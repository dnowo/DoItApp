import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, Subscription} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  authRequest: any = {
    username: 'danielnowo',
    password: 'doitapp'
  };

  token: any;

  private jobSubject = new BehaviorSubject<Job[]>([]);
  jobObservable$ = this.jobSubject.asObservable();
  jobs = [];

  constructor(private http: HttpClient) {
    this.getAccessToken(this.authRequest);
  }

  private generateToken(request): Observable<any> {
    return this.http.post('http://localhost:8080/login', request, {responseType: 'text' as 'json'});
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

  public getAllJobs(): Observable<Job[]>{
    const headers = this.generateAuthorizedHeader();
    // return this.http.get<Job[]>('http://localhost:8080/api/job/all', { headers });
    this.http.get<Job[]>('http://localhost:8080/api/job/all', {headers}).subscribe(j => {
      this.jobs = j;
      this.jobSubject.next(j);
    }, error => {
      console.log(error);
    });
    return this.jobObservable$;
  }

  public getJobById(id: number): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Job>('http://localhost:8080/api/job/' + id, {headers});
  }

  public addJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.post<Job>('http://localhost:8080/api/job/add', job, {headers});
  }

  public editJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.put<Job>('http://localhost:8080/api/job/edit', job, {headers});
  }

  public deleteJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    const index: number = this.jobs.indexOf(job);
    if (index !== -1) {
      this.jobs.splice(index, 1);
    }
    this.jobSubject.next(this.jobs);
    return this.http.delete<Job>('http://localhost:8080/api/job/delete/' + job.id, {headers});
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
