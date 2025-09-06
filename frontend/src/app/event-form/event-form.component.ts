import { Component, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  FormArray,
  Validators,
} from '@angular/forms';
import {
  HttpClientModule,
  HttpClient,
  HttpErrorResponse,
} from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [ReactiveFormsModule, HttpClientModule, CommonModule],
  templateUrl: './event-form.component.html',
  styleUrl: './event-form.component.scss',
})
export class EventFormComponent implements OnInit {
  eventForm!: FormGroup;
  successMessage = '';
  htmlLink = '';
  meetLink = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.eventForm = this.fb.group({
      summary: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      emails: this.fb.array([this.fb.control('', Validators.email)]),
    });

    // PARTE 2: Cargar datos guardados del localStorage al iniciar el componente
    const savedData = localStorage.getItem('eventFormData');
    if (savedData) {
      const data = JSON.parse(savedData);
      this.eventForm.patchValue({
        summary: data.summary,
        startTime: data.startTime,
        endTime: data.endTime,
      });
      this.setEmails(data.emails);
      // Limpiar los datos después de usarlos para que no se muestren en visitas futuras
      localStorage.removeItem('eventFormData');
    }
  }

  setEmails(emails: string[]) {
    const emailsFormArray = this.fb.array([]);
    emails.forEach((email) => {
      emailsFormArray.push(this.fb.control(email, Validators.email));
    });
    this.eventForm.setControl('emails', emailsFormArray);
  }

  get emailsFormArray(): FormArray {
    return this.eventForm.get('emails') as FormArray;
  }

  addEmail() {
    this.emailsFormArray.push(this.fb.control('', Validators.email));
  }

  removeEmail(index: number) {
    this.emailsFormArray.removeAt(index);
  }

  logout() {
    // Llama al servicio de autenticación para cerrar la sesión
    this.authService.logout();

    // Redirige al usuario a la página de login
    this.router.navigate(['/login']);
  }

  onCreateEvent() {
    const userId = this.authService.getCurrentUser();
    if (!userId) {
      alert('No hay usuario autenticado. Redirigiendo al login.');
      this.authService.logout();
      return;
    }

    const eventData = {
      summary: this.eventForm.value.summary,
      startTime: this.eventForm.value.startTime,
      endTime: this.eventForm.value.endTime,
      emails: this.eventForm.value.emails,
    };

    const backendUrl = `http://localhost:8080/api/createEvent/${userId}`;

    this.http
      .post<any>(backendUrl, eventData)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            console.error(
              'Usuario no autenticado, redirigiendo para autorización.'
            );
            // PARTE 1: Guardar los datos del formulario antes de la redirección
            localStorage.setItem('eventFormData', JSON.stringify(eventData));

            const authUrl = `http://localhost:8080/api/authorize?userId=${userId}`;

            // Abre la URL en una nueva ventana emergent
            const popup = window.open(authUrl, '_blank', 'width=600,height=600,left=200,top=200');

            // Monitorea cuando la ventana emergente se cierre para recargar la página
            const timer = setInterval(() => {
              if (popup && popup.closed) {
                clearInterval(timer);
                window.location.reload(); // Recarga la página para reintentar la creación del evento
              }
            }, 500);

            return throwError(() => new Error('Redirección a autorización.'));
          } else {
            console.error('Error al crear el evento:', error);
            alert(
              'Error al crear el evento. Revisa la consola para más detalles.'
            );
            return throwError(() => new Error('Error en el servidor.'));
          }
        })
      )
      .subscribe((response) => {
        this.successMessage = 'Evento creado con éxito.';
        this.htmlLink = response.htmlLink;
        this.meetLink = response.meetLink;
        alert(this.successMessage);
      });
  }
}
