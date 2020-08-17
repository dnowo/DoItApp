import { Component } from '@angular/core';
import {ApiService, Job} from './api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'DoItApp';
  jobs: any;
  constructor(private service: ApiService) { }

  getJobs(): void{
    this.service.getAllJobs().subscribe(jobs => this.jobs = jobs);
  }

  getJobById(id: number): void {
    this.service.getJobById(id).subscribe(jobs => {
      this.jobs = jobs;
      console.log(jobs);
    });

  }

  addPost(): void{
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
}

