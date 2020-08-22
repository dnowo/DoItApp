import {Component, Pipe, PipeTransform, ViewChild} from '@angular/core';
import {ApiService, Job} from './api.service';
import {Observable} from 'rxjs';
import {animate, style, transition, trigger} from '@angular/animations';
import {FormControl, FormGroupDirective, FormGroup, NgForm, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({transform: 'translateY(-50%)'}),
        animate('300ms ease-in', style({transform: 'translateY(30%)'}))
      ]),
      transition(':leave', [
        animate('150ms ease-in', style({transform: 'translateY(-50%)'}))
      ])
    ])
  ]
})

export class AppComponent {

  constructor(private service: ApiService, public dialog: MatDialog) {
    this.delay(150);
  }
  title = 'DoItApp';
  selectedJob: Job;
  jobToAdd: Job;
  allJobs$: Observable<Job[]>;

  form = new FormGroup({
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

  jobAddEdit: Job = new class implements Job {
    deadline: Date;
    description: string;
    ended: boolean;
    id: number;
    notification: boolean;
    priority: number;
    title: string;
  };

  // getJobById(id: number): void {
  //   this.service.getJobById(id).subscribe(jobs => {
  //     this.jobs = jobs;
  //   });
  // }
  numberOfJobs = 0;

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => {
      this.getJobs(this.numberOfJobs);
    });

  }

  getJobs(page: number): void {
    this.allJobs$ = this.service.getAllJobs(page);
  }

  addJob(): void {
    this.jobAddEdit = this.readForm();
    this.service.addJob(this.jobAddEdit).subscribe(jobs => console.log(jobs));
    this.jobAddEdit = null;
    this.jobToAdd = null;
  }

  editJob(id: number): void {
    this.jobAddEdit = this.readForm();
    this.jobAddEdit.id = id;
    this.service.editJob(this.jobAddEdit).subscribe(edited => console.log(edited));
    this.selectedJob = null;
    this.jobAddEdit = null;
  }

  deleteJob(job: Job): void {
    this.service.deleteJob(job).subscribe(deleted => console.log(deleted));
  }

  onSelect(j: Job): void {
    this.selectedJob = j;
    this.form.patchValue({
      notification: j.notification,
    });
  }

  onAdd(): void {
    this.jobToAdd = ({
      id: null,
      title: 'Type in title...',
      description: 'Description...',
      priority: 0,
      notification: false,
      deadline: null,
      ended: false,
    });
  }

  readForm(): Job {
    const job: Job = new class implements Job {
      deadline: Date;
      description: string;
      ended: boolean;
      id: number;
      notification: boolean;
      priority: number;
      title: string;
    };
    job.title = this.form.value.title;
    job.priority = this.form.value.priority;
    job.deadline = this.form.value.deadline;
    job.description = this.form.value.description;
    job.notification = this.form.value.notification;

    return job;
  }

  finishJob(j: Job): void {
    j.ended = true;
    this.service.editJob(j).subscribe(job => console.log(job));
    this.selectedJob = null;
    this.jobAddEdit = null;
  }

  openErrDialog(): void {
    this.dialog.open(DialogErrorAction);
  }

}

@Component({
  selector: 'dialog-error-action',
  templateUrl: 'dialog-error-action.html',
})
export class DialogErrorAction {}

@Pipe({
  name: 'split'
})
export class SplitPipe implements PipeTransform {
  transform(val: string, params: string[]): string[] {
    return val.split(params[0]);
  }
}
