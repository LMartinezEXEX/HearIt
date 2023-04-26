import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { SpotifyService } from "src/app/services/spotify/spotify.service";
import { UsersService } from "src/app/services/user/user.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.css"]
})
export class LoginComponent {
  
  username: string;
  password: string;

  error: boolean = false;

  constructor(
    public userService: UsersService, 
    public spotifyService: SpotifyService, 
    public router: Router,
    public cookies: CookieService) {
    }

  login(loginForm: any) {
    if (loginForm.invalid) {
      return;
    }
    
    this.username = String(loginForm.value.username);
    this.password = String(loginForm.value.password);

    const user = { username: this.username, password: this.password }
    this.userService.login(user).subscribe({
      error: (_) => this.error = true,
      next: (data) => {
        this.userService.setToken(data?.token);
        this.userService.setId(data?.id);
        
        if (data?.spotifyCode) {
          this.spotifyService.getAccessToken(data?.spotifyCode).subscribe( data => {
            this.cookies.set('spotify_access_token', data?.access_token, data?.expires_in);
          });
        } else {
          window.location.href=data?.accessUrl;
        }

        this.router.navigateByUrl('/home');
      }
    });
  }
}