import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  userEmail: string = '';
  password: string = '';
  message: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  onRegister() {
    const registerData = {
      email: this.userEmail,
      password: this.password
    };

    this.http.post('http://localhost:8080/api/auth/register', registerData, { responseType: 'text' })
      .subscribe(
        () => {
          this.message = 'Registro exitoso. ¡Inicia sesión ahora!';
          this.router.navigate(['/login']);
        },
        error => {
          this.message = 'Error en el registro: ' + error.error;
          console.error('Error de registro:', error);
        }
      );
  }
}