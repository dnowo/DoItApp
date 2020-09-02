import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {AuthorizeService} from '../_service/authorize.service';
import {TokenService} from '../_service/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  isLogged = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(private authService: AuthorizeService, private tokenStorage: TokenService) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLogged = true;
      // this.roles = this.tokenStorage.getUser().roles;
    }
  }

  submit(): void {
    if (this.form.valid) {
      this.authService
        .login(this.form)
        .subscribe(data => {
          this.tokenStorage.setToken(data);
          // this.tokenStorage.setUser(data);

          this.isLoginFailed = false;
          this.isLogged = true;
          // this.roles = this.tokenStorage.getUser().roles;
          window.location.href = '/home';
        });
    }
  }



}
