import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { UsersService } from "src/app/services/user/user.service";

@Component({
    selector: "app-register",
    templateUrl: "./register.component.html",
    styleUrls: ["./register.component.css"]
})
export class RegisterComponent {
  username: string;
  password: string;

  created: boolean = false;
  error: boolean = false;

  constructor(public userService: UsersService, public router: Router) {}

  register(registerForm: any) {
    if (registerForm.invalid) {
      return;
    }

    this.username = String(registerForm.value.username);
    this.password = String(registerForm.value.password);
    const confirmPassword = String(registerForm.value.confirmPassword);

    if (this.password !== confirmPassword) {
      return;
    }

    const user = { username: this.username, password: this.password }
    this.userService.register(user).subscribe({
      error: (_) => {
        this.error = true;
        this.created = false;
      },
      next: (_) => {
        this.created = true;
        this.error = false;
      }
    });
  }
}