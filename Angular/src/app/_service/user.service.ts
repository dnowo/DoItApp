import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

const API = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private jobSubject = new BehaviorSubject<Job[]>([]);
  jobObservable$ = this.jobSubject.asObservable();
  jobs = [];
  jobsUnsorted = [];

  constructor(private http: HttpClient) { }

  getAllJobs(page: number): Observable<Job[]> {
    this.http.get<Job[]>(API + 'api/job/all' + '?page=' + page).subscribe(j => {
      if (j.length > 0){
        this.jobs = j;
        console.log(j);
        this.jobSubject.next(j);
      }
    }, error => {
      console.log(error);
    });
    return this.jobObservable$;
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
