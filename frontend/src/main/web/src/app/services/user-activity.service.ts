import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from "../entities/user";
import {Observable, of, throwError} from "rxjs";
import {catchError, tap, finalize} from "rxjs/operators";
import {Router} from "@angular/router";
import {UserInvite} from '../entities/user-invite';
import {Announcement} from "../entities/announcement";
import {UserService} from "./user.service";
import {UserActivity} from "../entities/user-activity";

@Injectable({
  providedIn: 'root'
})
export class UserActivityService {
  private apiURL = 'http://localhost:8080/api/activity';
  //private apiURL = '/api/activity';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private userService: UserService) { }

  private handleError<T>(operation= 'operation') {
    return (error: any): Observable<T> => {
      console.log(operation + ' ' + error);
      return throwError(error);
    };
  }

  getAll() {
    return this.http.get<UserActivity[]>(this.apiURL + '/all',  {
      headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.userService.getToken()}`)
    }).pipe(
      catchError(this.handleError<any>('getAll'))
    );
  }
}
