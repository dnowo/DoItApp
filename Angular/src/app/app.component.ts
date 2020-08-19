import {Component} from '@angular/core';
import {ApiService, Job} from './api.service';
import {Observable} from 'rxjs';
import {animate, style, transition, trigger} from '@angular/animations';
import {FormControl, FormGroupDirective, FormGroup, NgForm, Validators} from '@angular/forms';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({transform: 'translateY(-100%)'}),
        animate('300ms ease-in', style({transform: 'translateY(0%)'}))
      ]),
      transition(':leave', [
        animate('150ms ease-in', style({transform: 'translateY(-100%)'}))
      ])
    ])
  ]
})

export class AppComponent{
  title = 'DoItApp';
  jobs: any;
  selectedJob: Job;
  allJobs$: Observable<Job[]>;

  editForm = new FormGroup({
    notification: new FormControl('', [
      Validators.required
    ]),
    title: new FormControl('', [
      Validators.required,
      Validators.minLength(2)
    ]),
    description: new FormControl('', [
      Validators.required,
      Validators.minLength(2)
    ]),
    deadline: new FormControl('', [
      Validators.required,
    ]),
    priority: new FormControl('', [
      Validators.required,
    ]),
  });

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

  editJob(job: Job): void {
    this.service.editJob(job).subscribe(edited => console.log(edited));
    this.selectedJob = null;
  }

  deleteJob(job: Job): void {
    this.service.deleteJob(job).subscribe(deleted => console.log(deleted));
  }

  onSelect(j: Job): void {
    this.selectedJob = j;
    this.editForm.patchValue({
      notification: j.notification,
    });
  }

  readEditForm(id: number): void{
    const editedJob: Job = new class implements Job {
      deadline: Date;
      description: string;
      ended: boolean;
      id: number;
      notification: boolean;
      priority: number;
      title: string;
    };

    editedJob.id = id;
    editedJob.title = this.editForm.value.title;
    editedJob.priority = this.editForm.value.priority;
    editedJob.deadline = this.editForm.value.deadline;
    editedJob.description = this.editForm.value.description;
    editedJob.notification = this.editForm.value.notification;

    this.editJob(editedJob);
  }
}

