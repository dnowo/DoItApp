import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  authRequest: any = {
    username: 'danielnowo',
    password: 'doitapp'
  };

  response: any;

  token: any;

  constructor(private http: HttpClient) {
    this.getAccessToken(this.authRequest);
  }

  private generateToken(request): Observable<any> {
    return this.http.post('http://localhost:8080/login', request, { responseType: 'text' as 'json' });
  }

  public getAccessToken(authRequest): void {
    const resp = this.generateToken(authRequest);
    resp.subscribe(data => {
      this.token = data;
    });
  }

  private generateAuthorizedHeader(): HttpHeaders {
    const tokenStr = 'Bearer ' + this.token;
    const generatedHeader = new HttpHeaders().set('Authorization', tokenStr);
    return generatedHeader;
  }

  public getAllJobs(): Observable<Job[]> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Job[]>('http://localhost:8080/api/job/all', { headers });
  }

  public getJobById(id: number): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Job>('http://localhost:8080/api/job/' + id, { headers });
  }

  public addJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.post<Job>('http://localhost:8080/api/job/add', job, { headers });
  }

  public editJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.put<Job>('http://localhost:8080/api/job/edit', job,{ headers });
  }

  public deleteJob(id: number): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.delete<Job>('http://localhost:8080/api/job/delete/' + id, { headers });
  }

}

export interface Job {
  id: number;
  priority: number;
  title: string;
  description: string;
  deadline: Date;
  notification: boolean;
  ended: boolean;
}
