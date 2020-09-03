import { Component, OnInit } from '@angular/core';
import {User, UserService} from '../_service/user.service';
import {TokenService} from '../_service/token.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  isLogged: boolean;
  // tslint:disable-next-line:new-parens
  user: User = new class implements User {
    username: string;
    email: string;
  };

  constructor(private userService: UserService, private tokenService: TokenService){}

  ngOnInit(): void {
    if (this.tokenService.getToken() != null) {
      this.isLogged = true;
      this.getUserData();
    }
  }

  private getUserData(): void{
    this.userService.getUserData()
      .subscribe(u => {
        this.user = u;
      }, error => {
        if (error.status === 401) {
          this.logout();
        }
      });
  }

  logout(): void {
    this.tokenService.logout();
    window.location.href = '/login';
  }

}

