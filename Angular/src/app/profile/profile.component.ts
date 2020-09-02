import { Component, OnInit } from '@angular/core';
import {UserService} from '../_service/user.service';
import {TokenService} from '../_service/token.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  isLogged: boolean;

  constructor(private userService: UserService, private tokenService: TokenService){}

  ngOnInit(): void {
    if (this.tokenService.getToken() != null) { this.isLogged = true; }
  }

  logout(): void {
    this.tokenService.logout();
    window.location.reload();
  }

}
