import {Component, OnInit, Input, ViewChild, AfterViewChecked, AfterViewInit, ElementRef} from '@angular/core';
import {FormControl} from '@angular/forms';
import {UserService} from '../services/user.service';
import { QuizService } from '../services/quiz.service';
import { User } from '../entities/user';
import { Quiz } from '../entities/quiz';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import {Location} from '@angular/common';
import {CategoryService} from '../services/category.service';
import {Category} from '../entities/category';
import {AchievementService} from "../services/achievement.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DEBOUNCE_TIME, USER_DEFAULT_IMAGE} from "../parameters";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  @ViewChild('closeModal') closeModal: ElementRef;
  tab = '';
  user: User;
  imageUrl: string = USER_DEFAULT_IMAGE;
  users$: Observable<User[]>;
  quizes$: Observable<Quiz[]>;
  categoriesList: Category[] = [];
  isVisible = true;
  term: string = "";
  selectedCategories: string[] = [];
  dateFrom: Date;
  dateTo: Date;
  quizUser: string = "";
  joinForm: FormGroup;
  join_message=false;
  isMoreFiltersVisible = false;


  private searchQuizTerms = new Subject<any>();
  private searchUserTerms = new Subject<string>();

  constructor(private userService: UserService, private quizService: QuizService, private achievementService: AchievementService,
              private categoryService: CategoryService,
              private location: Location, private route: ActivatedRoute,
              private router: Router,
              private fb: FormBuilder,
    ) { }

  ngOnInit(): void {
    this.getCategories();
    this.tab = this.route.snapshot.paramMap.get('tab');
    this.user = this.tab === 'Profile' ? this.userService.user : {role: {}} as User;
    this.user.image = this.getUserImage();
    this.checkImage();
    this.quizes$ = this.searchQuizTerms.pipe(
      debounceTime(DEBOUNCE_TIME),
      distinctUntilChanged(),
      switchMap((obj: any) => this.quizService.searchQuizzes(obj.title, obj.categories, obj.dateFrom, obj.dateTo, obj.user)),
    );
    this.users$ = this.searchUserTerms.pipe(
      debounceTime(DEBOUNCE_TIME),
      distinctUntilChanged(),
      switchMap((term: string) => this.userService.searchUsers(term)),
    );
    this.setJoinForm();

  }

  search(): void {
    let date_from;
    let date_to;
    if (this.dateFrom === undefined) {
      date_from = "2020-01-01";
    } else {
      date_from = this.dateFrom.toJSON().slice(0, 8) + (this.dateFrom.getDate() < 10 ? "0" + this.dateFrom.getDate() : this.dateFrom.getDate());
    }
    if (this.dateTo === undefined) {
      this.dateTo = new Date();
      date_to = this.dateTo.toJSON().slice(0, 8) + (this.dateTo.getDate() < 10 ? "0" + this.dateTo.getDate() : this.dateTo.getDate());
    } else {
      date_to = this.dateTo.toJSON().slice(0, 8) + (this.dateTo.getDate() < 10 ? "0" + this.dateTo.getDate() : this.dateTo.getDate());
    }
    this.isVisible = false;
    if (this.tab === 'Quizzes') {
      this.searchQuizTerms.next({ title: this.term, categories: this.selectedCategories, dateFrom: date_from, dateTo: date_to, user: this.quizUser });
    } else {
      this.searchUserTerms.next(this.term);
    }
  }
  profileSet( editOnly?: boolean, user?: User) {
    this.user = (user ? user : {role: {}} as User);
    this.changeTab(editOnly ? 'AddProfile' : 'Profile');
  }

  changeTab(tab: string) {
    this.tab = tab;
    this.router.navigateByUrl(`dashboard/${tab}`);
    this.isVisible = true;
  }

  showMoreFilters() {
    this.isMoreFiltersVisible = !this.isMoreFiltersVisible;
  }

  getUser() {
    return this.userService.user;
  }
  getUserName() {
    return this.userService.user.firstName;
  }
  getUserLastName() {
    return this.userService.user.lastName;
  }
  getUserRole() {
    return this.userService.user.role.name;
  }
  getUserImage() {
    return this.userService.user.image;
  }

  getCategories() {
    this.categoryService.getCategories().subscribe(
      (data: Category[]) => {
        this.categoriesList = data;
      }
    )
  }

  private setJoinForm() {
    this.joinForm = this.fb.group({
      accessCode: ["", [Validators.required, Validators.minLength(3)]]
    } );
  }
  connectToSession(accessCode:string){
    this.quizService.joinSession(accessCode).subscribe(res => {
      this.userService.user.joined=true;
      this.router.navigate(['/quiz/' + res.quiz_id + '/' + res.id]);
      this.closeModal.nativeElement.click()
    }, error => {console.log(error.error);console.log("JOIN MES"+this.join_message)})
  }

  submit() {
    this.connectToSession(this.joinForm.get('accessCode').value);
  }
  checkImage() {
    if (this.user.image != null) {
      this.imageUrl = this.user.image;
    }
  }

}
