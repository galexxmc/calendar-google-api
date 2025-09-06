import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login', // <--- Se corrigió el selector aquí
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './login.component.html', // <--- Se corrigió el nombre del archivo aquí
  styleUrls: ['./login.component.scss'] // <--- Se corrigió el nombre del archivo aquí
})
export class LoginComponent implements OnInit { // <--- Se corrigió el nombre de la clase aquí
  userEmail: string = '';
  password: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    if (this.authService.getCurrentUser()) {
      this.router.navigate(['/event-form']);
    }
  }

  onLogin() {
    const loginData = {
      email: this.userEmail,
      password: this.password
    };

    this.http.post('http://localhost:8080/api/auth/login', loginData, { responseType: 'text' })
      .subscribe(
        () => {
          this.authService.login(this.userEmail);
          this.router.navigate(['/event-form']);
        },
        error => {
          alert('Credenciales incorrectas. Inténtalo de nuevo.');
          console.error('Error de autenticación:', error);
        }
      );
  }
}