import {Component} from '@angular/core';
import {ApiService, Job} from './api.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'DoItApp';
  jobs: any;
  job: Array<Job>;

  allJobs$: Observable<Job[]>;

  constructor(private service: ApiService) {
    this.delay(100);
  }

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => {
      this.getJobs();
    });
  }

  getJobs(): void {
    // this.service.getAllJobs().subscribe(j => this.job = j, (error: HttpErrorResponse) => {
    //   console.log(error.status, error.headers);
    // });
    this.allJobs$ = this.service.getAllJobs();
    // console.log(this.job.filter(job => job.id === 2));
  }

  getJobById(id: number): void {
    this.service.getJobById(id).subscribe(jobs => {
      this.jobs = jobs;
      console.log(jobs);
    });

  }

  addPost(): void {
    const job: Job = ({
      id: null,
      title: 'Nowa praca Angular!',
      description: 'Lorem ipsum...[..]',
      priority: 2,
      notification: false,
      deadline: new Date('2020-08-09T22:47:00'),
      ended: false,
    });
    this.service.addJob(job).subscribe(jobs => console.log(jobs));
  }

  editPost(): void {
    const job: Job = ({
      id: 3,
      title: 'Edytowana praca Angular!',
      description: 'Lorem ipsum...[..]',
      priority: 1,
      notification: false,
      deadline: new Date('2020-08-18T09:46:00'),
      ended: false,
    });
    this.service.editJob(job).subscribe(edited => console.log(edited));
  }

  deletePost(): void {
    this.service.deleteJob(1).subscribe(deleted => console.log(deleted));
  }
}

