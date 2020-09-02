import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API = 'http://localhost:8080/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthorizeService {

  constructor(private http: HttpClient) { }

  login(credentials: any): Observable<any> {
    const authRequest: any = {
      username: credentials.value.username,
      password: credentials.value.password
    };
    return this.http.post(API + 'login', authRequest, { responseType: 'text' as 'json' });
  }

  register(user: any): Observable<any> {
    return null;
  }
}
