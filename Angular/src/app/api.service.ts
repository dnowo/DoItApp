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

  public generateToken(request) {
    return this.http.post('http://localhost:8080/login', request, { responseType: 'text' as 'json' });
  }

  public getAccessToken(authRequest): void {
    const resp = this.generateToken(authRequest);
    resp.subscribe(data => {
      this.token = data;
    });
  }

  public generateAuthorizedHeader(): HttpHeaders {
    const tokenStr = 'Bearer ' + this.token;
    const generatedHeader = new HttpHeaders().set('Authorization', tokenStr);
    return generatedHeader;
  }

  public getAllJobs(): Observable<Array<Job>> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Array<Job>>('http://localhost:8080/api/job/all', { headers, responseType: 'text' as 'json' });
  }

  public getJobById(id: number): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Job>('http://localhost:8080/api/job/' + id, { headers });
  }

  public addJob(job: Job): Observable<Job>{
    const headers = this.generateAuthorizedHeader();
    return this.http.post<Job>('http://localhost:8080/api/job/add', job, { headers });
  }
}

export interface Job {
  id: bigint;
  priority: number;
  title: string;
  description: string;
  deadline: Date;
  notification: boolean;
  ended: boolean;
}
