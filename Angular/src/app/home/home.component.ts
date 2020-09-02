import {Component, OnInit, Pipe, PipeTransform} from '@angular/core';
import {Job} from '../_service/user.service';
import {interval, Observable} from 'rxjs';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../_service/user.service';
import {animate, style, transition, trigger} from '@angular/animations';
import {Howl} from 'howler';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
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
export class HomeComponent implements OnInit {

  selectedJob: Job = null;
  jobToAdd: Job = null;
  allJobs$: Observable<Job[]>;
  pageNumber = 0;
  oneJob: any;
  maxPage: number;

  notifyCheck: any;
  notifyOnOff: any;

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

  // tslint:disable-next-line:new-parens
  jobAddEdit: Job = new class implements Job {
    deadline: Date;
    description: string;
    ended: boolean;
    id: number;
    notification: boolean;
    priority: number;
    title: string;
  };
  private dialog: any;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getJobs(this.pageNumber);
  }

  // getJobById(id: number): void {
  //   this.service.getJobById(id).subscribe(job => {
  //     this.oneJob = job;
  //   });
  // }

  // async delay(ms: number) {
  //   await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => {
  //     this.getJobs(this.pageNumber);
  //   });
  //
  // }

  getJobs(page: number): void {
    this.allJobs$ = this.userService.getAllJobs(page);
    this.allJobs$.subscribe(data => console.log(data));
  }

  goNextPage(): void {
    this.maxPage = this.userService.jobs.length / 6;
    if (this.pageNumber <= this.maxPage ) {
      this.pageNumber++;
    }
  }

  goPrevPage(): void {
    if (this.pageNumber === 0) { return; }
    if (this.pageNumber > 0) { this.pageNumber--; }
  }

  addJob(): void {
    this.jobAddEdit = this.readForm();
    this.userService.addJob(this.jobAddEdit).subscribe(jobs => console.log(jobs));
    this.jobAddEdit = null;
    this.jobToAdd = null;
  }

  editJob(id: number): void {
    this.jobAddEdit = this.readForm();
    this.jobAddEdit.id = id;
    this.jobAddEdit.ended = false;
    this.userService.editJob(this.jobAddEdit).subscribe(edited => console.log(edited));
    this.selectedJob = null;
    this.jobAddEdit = null;
  }

  deleteJob(job: Job): void {
    this.userService.deleteJob(job).subscribe(deleted => console.log(deleted));
    console.log(job);
  }

  onSelect(j: Job): void {
    if (this.jobToAdd === null) {
      this.selectedJob = j;
      this.form.patchValue({
        notification: j.notification,
      });
    } else {
      this.jobToAdd = null;
      this.selectedJob = j;
      this.form.patchValue({
        notification: j.notification,
      });
    }
  }

  onAdd(): void {
    if (this.selectedJob === null) {
      this.form.patchValue({
        notification: false,
        priority: 0,
        deadline: null,
        description: '',
        title: ''
      });
      this.jobToAdd = ({
        id: null,
        title: 'Type in title...',
        description: 'Description...',
        priority: 0,
        notification: false,
        deadline: null,
        ended: false,
      });
    } else {
      this.selectedJob = null;
      this.form.patchValue({
        notification: false,
        priority: 0,
        deadline: null,
        description: '',
        title: ''
      });
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
    this.userService.editJob(j).subscribe(job => console.log(job));
    this.selectedJob = null;
    this.jobAddEdit = null;
  }

  offNotification(j: Job): void {
    this.notifyOnOff = !this.notifyOnOff;
    j.notification = !j.notification;
    this.userService.editJob(j).subscribe(job => console.log(job));
    this.selectedJob = null;
    this.jobAddEdit = null;
  }

  openErrDialog(): void {

  }

  notify(): void {
    this.notifyCheck = interval(5000)
      .subscribe((val) => {
        this.userService.getAllJobsUnsorted();
        this.userService.jobsUnsorted.filter(vale => {
          if (vale.ended === true && vale.notification === true) {
            const sound = new Howl({
              src: ['/assets/notify.mp3']
            });
            sound.play();
            this.offNotification(vale);
          }
        });
      });
  }
}
