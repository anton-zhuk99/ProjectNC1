import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "./user.service";
import {Observable} from "rxjs";
import {Notification} from '../entities/notification';
import {Setting} from '../entities/setting';

import {tap} from "rxjs/operators";
import {User} from "../entities/user";
@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  notificationSettings: Notification[];
  activitySettings: Setting[];


  private apiURL = 'http://localhost:8080/api';

  // private apiURL = '/api';


  constructor(private http: HttpClient, private userService: UserService) { }


  getNotificationSettings(userId: number) {
    return  this.http.get<Notification[]>(this.apiURL + '/notification/settings/get/',
      { headers: new HttpHeaders()
          .set('Authorization',  `Bearer_${this.userService.getToken()}`)}).pipe(
      tap(response => {this.notificationSettings = response;}))
  }
  getActivitiesSettings(userId: number) {
    return  this.http.get<Setting[]>(this.apiURL + '/activity/get-settings',
      { headers: new HttpHeaders()
          .set('Authorization',  `Bearer_${this.userService.getToken()}`)}).pipe(
      tap(response => {this.activitySettings = response;}))
  }
  setNotificationSetting(notification) {
    return  this.http.post<Notification>(this.apiURL + '/notification/settings', notification,
      { headers: new HttpHeaders()
          .set('Authorization',  `Bearer_${this.userService.getToken()}`)})
  }
  setActivitySetting(setting) {
    return  this.http.post<Setting>(this.apiURL + '/activity/set-settings', setting,
      { headers: new HttpHeaders()
          .set('Authorization',  `Bearer_${this.userService.getToken()}`)})
  }
  setLanguage(lang: string, user: User) {
    return  this.http.put(this.apiURL + `/change/${lang}`, user,
      { headers: new HttpHeaders()
          .set('Authorization',  `Bearer_${this.userService.getToken()}`)})
  }
}
