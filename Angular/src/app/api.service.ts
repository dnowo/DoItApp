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
  private url = 'http://localhost:8080/';

  private jobSubject = new BehaviorSubject<Job[]>([]);
  jobObservable$ = this.jobSubject.asObservable();
  jobs = [];
  jobsUnsorted = [];

  constructor(private http: HttpClient) {
    this.getAccessToken(this.authRequest);
  }

  private generateToken(request): Observable<any> {
    return this.http.post(this.url + 'login', request, {responseType: 'text' as 'json'});
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

  public getAllJobs(page: number): Observable<Job[]> {
    const headers = this.generateAuthorizedHeader();
    this.http.get<Job[]>(this.url + 'api/job/all' + '?page=' + page, {headers}).subscribe(j => {
      if (j.length > 0){
        this.jobs = j;
        this.jobSubject.next(j);
      }
    }, error => {
      console.log(error);
    });
    return this.jobObservable$;
  }

  public getAllJobsUnsorted() {
    const headers = this.generateAuthorizedHeader();
    this.http.get<Job[]>(this.url + 'api/job/unsorted', {headers}).subscribe(j => {
      this.jobsUnsorted = j;
    });
  }

  public getJobById(id: number): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    return this.http.get<Job>(this.url + 'api/job/' + id, {headers});
  }

  public addJob(job: Job): Observable<Job> {
    // @ts-ignore
    job.deadline = this.convert(job.deadline);
    this.jobs.push(job);
    this.jobSubject.next(this.jobs);
    const headers = this.generateAuthorizedHeader();
    return this.http.post<Job>(this.url + 'api/job/add', job, {headers});
  }

  public editJob(job: Job): Observable<Job> {
    const find: Job = this.jobs.find(x => x.id === job.id);
    const index: number = this.jobs.indexOf(find);

    // @ts-ignore
    job.deadline = this.convert(job.deadline);
    console.log(job.deadline);
    if (index !== -1) {
      this.jobs[index].title = job.title;
      this.jobs[index].description = job.description;
      this.jobs[index].deadline = job.deadline;
      this.jobs[index].notification = job.notification;
      this.jobs[index].priority = job.priority;
      this.jobs[index].ended = job.ended;
      this.jobSubject.next(this.jobs);
    }
    const headers = this.generateAuthorizedHeader();
    return this.http.put<Job>(this.url + 'api/job/edit', job, {headers});
  }

  public deleteJob(job: Job): Observable<Job> {
    const headers = this.generateAuthorizedHeader();
    const index: number = this.jobs.indexOf(job);
    if (index !== -1) {
      this.jobs.splice(index, 1);
    }
    this.jobSubject.next(this.jobs);
    return this.http.delete<Job>(this.url + 'api/job/delete/' + job.id, {headers});
  }

  public convert(str): string {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2),
      hour = date.getHours();
    const mins = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
    const secs = (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
    return [date.getFullYear(), mnth, day].join('-') + 'T' + [hour, mins, secs].join(':');
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
