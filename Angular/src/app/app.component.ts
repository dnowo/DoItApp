import {Component} from '@angular/core';
import {ApiService, Job} from './api.service';
import {Observable} from 'rxjs';
import {animate, style, transition, trigger} from '@angular/animations';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({transform: 'translateY(-100%)'}),
        animate('200ms ease-in', style({transform: 'translateY(0%)'}))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({transform: 'translateY(-100%)'}))
      ])
    ])
  ]
})
export class AppComponent{
  title = 'DoItApp';
  jobs: any;
  job: Array<Job>;
  selectedJob: Job;
  allJobs$: Observable<Job[]>;

  notificationEdit: any = false;
  editedJob: Job = new class implements Job {
    deadline: Date;
    description: string;
    ended: boolean;
    id: number;
    notification: boolean;
    priority: number;
    title: string;
  };

  constructor(private service: ApiService) {
    this.delay(100);
  }

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => {
      this.getJobs();
    });
  }

  getJobs(): void {
    this.allJobs$ = this.service.getAllJobs();
  }

  getJobById(id: number): void {
    this.service.getJobById(id).subscribe(jobs => {
      this.jobs = jobs;
      console.log(jobs);
    });

  }

  addJob(): void {
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

  editJob(): void {
    // const j: Job = ({
    //   id: 3,
    //   title: 'Edytowana praca Angular!',
    //   description: 'Lorem ipsum...[..]',
    //   priority: 1,
    //   notification: false,
    //   deadline: new Date('2020-08-18T09:46:00'),
    //   ended: false,
    // });
    this.service.editJob(this.editedJob).subscribe(edited => console.log(edited));
    this.editedJob = null;
    this.selectedJob = null;
  }

  deleteJob(job: Job): void {
    this.service.deleteJob(job).subscribe(deleted => console.log(deleted));
  }

  onSelect(j: Job): void {
    this.selectedJob = j;
  }

  readForm(id: number, title: string, deadline: Date, description: string, priority: string): void{
    console.log(this.notificationEdit, title, deadline, description, priority);
    this.editedJob.id = id;
    this.editedJob.title = title;
    this.editedJob.priority = parseInt(priority);
    this.editedJob.deadline = new Date(deadline);
    this.editedJob.description = description;
    this.editedJob.notification = this.notificationEdit;
  }

}

