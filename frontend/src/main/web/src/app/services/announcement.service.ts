import { Injectable } from '@angular/core';
import {User} from "../entities/user";
import {catchError} from "rxjs/operators";
import {Observable, throwError} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Announcement} from "../entities/announcement";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService {
  private apiURL = 'http://localhost:8080/api/announcement';
  // apiURL = '/api/announcement';
  private readonly token: string;
  private httpHeader: HttpHeaders;

  constructor(private http: HttpClient, private userService: UserService) {
    this.token = this.userService.getToken();
    this.httpHeader = new HttpHeaders().set('Authorization',  `Bearer_${this.token}`);
  }

  getAll(userId) {
    return this.http.get<Announcement[]>(this.apiURL + '/all',  {
      headers: this.httpHeader
    }).pipe(
      catchError(this.handleError<any>('getAll'))
    );
  }

  getCreated() {
    return this.http.get<Announcement[]>(this.apiURL + '/created',  {
      headers: this.httpHeader})
      .pipe(
      catchError(this.handleError<any>('getCreated'))
    );
  }

  deleteAnnouncement(id: number) {
    return this.http.delete<Announcement>(this.apiURL + `/delete/${id}`,{
      headers: this.httpHeader})
      .pipe(
      catchError(this.handleError<any>('deleteAnnouncement'))
    );
  }
  approve(ann: Announcement) {
    return this.http.post<Announcement>(this.apiURL + '/approve', ann, {
      headers: this.httpHeader})
      .pipe(
      catchError(this.handleError<any>('approve'))
    );
  }
  createAnnouncement(ann: Announcement) {
    return this.http.post<Announcement>(this.apiURL + '/create', ann, {
      headers: this.httpHeader})
      .pipe(
      catchError(this.handleError<any>('createAnnouncement'))
    );
  }

  updateAnnouncement(ann: Announcement) {
    return this.http.put<Announcement>(this.apiURL + `/update`, ann, {
      headers: this.httpHeader})
      .pipe(
      catchError(this.handleError<any>('updateAnnouncement'))
    );
  }

  private handleError<T>(operation= 'operation') {
    return (error: any): Observable<T> => {
      console.log(operation + ' ' + error);
      return throwError(error);
    };
  }
}
