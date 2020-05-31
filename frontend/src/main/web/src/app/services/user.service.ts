import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../entities/user';
import {Observable, of, throwError} from 'rxjs';
import {catchError, tap, finalize} from 'rxjs/operators';
import {Router} from '@angular/router';
import {UserInvite} from '../entities/user-invite';
import {ADMIN_ROLE_ID, MODER_ROLE_ID, USER_ROLE_ID} from '../parameters';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  authenticated = localStorage.getItem('user') ? true : false;
  user: User = JSON.parse(localStorage.getItem('user'));

  private userUrl = 'http://localhost:8080/api/user';
  private apiUrl = 'http://localhost:8080/api';

  // private userUrl = '/api/user';
  // private apiUrl = '/api';

  constructor(private http: HttpClient, private router: Router) {

  }
  getToken() {
    return this.user.token;
  }
  getUser(user: User): Observable<User> {
    return this.http.get<User>(this.userUrl + `/get/${user.id}`, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      catchError(this.handleError<any>('getUser'))
    );
  }
  updateUser(user: User) {
    return this.http.put<User>(this.userUrl + '/update', user, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      catchError(this.handleError<any>('updateUser'))
    );
  }
  createUser(user: User) {
    return this.http.post<User>(this.userUrl + '/create', user, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      catchError(this.handleError<any>('createUser'))
    );
  }

  deleteUser(user: User) {
    return this.http.delete<User>(this.userUrl + `/delete/${user.id}`, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      catchError(this.handleError<any>('deleteUser'))
    );
  }
  signUp(user: User): Observable<any> {
    return this.http.post<User>(this.apiUrl + '/sign-up', user, {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
      responseType: 'json'
    }).pipe(
      catchError(this.handleError<any>('signUp'))
    );

  }

  login(user): Observable<User> {
    return this.http.post<any>( this.apiUrl + '/login', user).pipe(
      tap(response => {
        this.user = response;
        localStorage.setItem('user', JSON.stringify(response));
        this.authenticated = true; }),
      catchError(this.handleError<any>('login'))
    );
  }
  logout() {
    return this.http.post<User>( this.apiUrl + '/logout', this.user, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(finalize(() => {
      this.authenticated = false; localStorage.clear(); this.router.navigateByUrl('/login');
    }), catchError(this.handleError<any>('logout')));
  }

  private handleError<T>(operation= 'operation') {
    return (error: any): Observable<T> => {
      return throwError(error);
    };
  }
  searchUsers(term: string): Observable<User[]> {
    if (!term.trim()) {
      return of([]);
    }
    const range = this.setRoleRange();
    return this.http.get<User[]>(this.userUrl + `/search/${term}/${range[0]}/${range[1]}`,
      { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      catchError(error => of([]))
    );
  }
  private setRoleRange() {
    let firstRole: number;
    let lastRole: number;
    if (this.user.role.name === 'super admin') {
      firstRole = MODER_ROLE_ID;
      lastRole = ADMIN_ROLE_ID;
    } else {
      if (this.user.role.name === 'admin') {
        firstRole = MODER_ROLE_ID;
        lastRole = MODER_ROLE_ID;
      } else {
        if (this.user.role.name === 'user') {
          firstRole = USER_ROLE_ID;
          lastRole = USER_ROLE_ID;
        }
      }
    }
    return [firstRole, lastRole];
  }
  sendUserInvite(userInvite: UserInvite): Observable<UserInvite> {
    return this.http.post<UserInvite>(this.userUrl + '/invite/send', userInvite, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
        tap(response => {
        }),
      catchError(this.handleError<any>('sendUserInvite'))
    );
  }

  getUserInvite(): Observable<UserInvite[]> {
    return this.http.get<UserInvite[]>(this.userUrl + '/invite/' + 'get', { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('getUserInvite'))
    );
  }

  acceptUserInvite(id) {
    return this.http.put<UserInvite>(this.userUrl + '/invite/' + id, id, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('acceptUserInvite')));
  }

  declineUserInvite(id) {
    return this.http.delete<UserInvite>(this.userUrl + '/invite/' + id, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('declineUserInvite')));
  }

  getFriendsList(): Observable<UserInvite[]> {
    return this.http.get<UserInvite[]>(this.userUrl + '/invite/friends/', { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('getFriendsList'))
    );
  }

  deleteFriendFromList(id) {
    return this.http.delete<UserInvite>(this.userUrl + '/invite/friends/' + id, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('deleteFriendFromList')));
  }

  recoveryPassword(email: string) {
    return this.http.post(this.apiUrl + '/recovery', JSON.stringify({email}) , {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
      responseType: 'json'
    }).pipe(
      catchError(this.handleError<any>('recoveryPassword'))
    );
  }
  setStatus(stausId, userId) {
    return this.http.post(this.userUrl + `/status/${stausId}`, undefined, { headers: new HttpHeaders()
        .set('Authorization',  `Bearer_${this.getToken()}`)}).pipe(
      tap(response => {
      }),
      catchError(this.handleError<any>('sendUserInvite'))
    );

  }
}
