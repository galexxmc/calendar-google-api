# API Calendar - Gestor de Eventos Google Calendar

Aplicación web completa para la creación y gestión de eventos en Google Calendar con invitaciones automáticas y enlaces de Google Meet.

## 📋 Descripción

**API Calendar** es una aplicación web que permite a los usuarios crear eventos de forma sencilla y automática en su Google Calendar personal. Una vez que inicies sesión, puedes acceder al formulario de creación de eventos donde podrás:

- **Crear eventos personalizados** que se guardan directamente en tu Google Calendar
- **Invitar múltiples personas** agregando sus correos electrónicos
- **Generar enlaces de Google Meet automáticamente** para cada evento
- **Sincronización automática** - Los invitados reciben la invitación y el evento aparece en sus calendarios

El sistema utiliza la **Google Calendar API** de Google Cloud para una integración completa y segura con los servicios de Google.

### ✨ Características Principales

- 🔐 **Autenticación OAuth2** con Google Calendar API
- 📅 **Creación de eventos** que se guardan en tu calendario personal
- 👥 **Sistema de invitaciones** - Agrega correos para invitar personas
- 🎥 **Google Meet automático** - Cada evento incluye un enlace de videollamada
- 📧 **Notificaciones automáticas** - Los invitados reciben la invitación por email
- 🔄 **Sincronización en tiempo real** - Los eventos aparecen en todos los calendarios
- 🚀 **Interfaz moderna** y fácil de usar
- ⚡ **Autorización automática** mediante ventana popup

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Web** - API REST
- **Spring Security** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **Google Calendar API v3** - Integración con Google Calendar
- **Google OAuth Client** - Autenticación OAuth2
- **MySQL** - Base de datos
- **Maven** - Gestión de dependencias

### Frontend
- **Angular 19** - Framework web moderno
- **TypeScript 5.7** - Lenguaje tipado
- **RxJS** - Programación reactiva
- **Angular Forms** - Formularios reactivos
- **HttpClient** - Cliente HTTP
- **SCSS** - Estilos avanzados

### Integración Google Cloud
- **Google Calendar API** - Gestión de calendarios
- **Google Meet API** - Generación automática de enlaces
- **OAuth 2.0** - Autenticación segura
- **Google Cloud Console** - Configuración de credenciales

## 🚀 Instalación y Configuración

### Pre-Eequisitos

- **Java 17 o superior**
- **Node.js 18+ y npm**
- **MySQL 8.0+**
- **Proyecto de Google Cloud** con Calendar API habilitada
- **IntelliJ IDEA** (recomendado para backend)
- **Visual Studio Code** (recomendado para frontend)

### 1. Configuración de Google Cloud

1. Crear proyecto en [Google Cloud Console](https://console.cloud.google.com/)
2. Habilitar **Google Calendar API**
3. Crear credenciales OAuth 2.0:
   - Tipo: **Aplicación web**
   - URI de redirección: `http://localhost:8080/api/callback`
4. Descargar archivo de credenciales JSON

### 2. Configuración de Base de Datos

```sql
CREATE DATABASE caldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configuración del Proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/galexxmc/google-calendar-api.git
   cd api-calendar
   ```

2. **Configurar credenciales del backend:**
   ```bash
   cp servicio-calendario/src/main/resources/application.properties.template servicio-calendario/src/main/resources/application.properties
   cp servicio-calendario/src/main/resources/credentials.json.template servicio-calendario/src/main/resources/credentials.json
   ```

3. **Editar `application.properties`:**
   ```properties
   google.oauth.clientId=TU_CLIENT_ID_DE_GOOGLE
   google.oauth.clientSecret=TU_CLIENT_SECRET_DE_GOOGLE
   spring.datasource.password=TU_PASSWORD_MYSQL
   ```

4. **Configurar `credentials.json`** con tus credenciales de Google Cloud

5. **Instalar dependencias del frontend:**
   ```bash
   cd frontend
   npm install
   ```

## 🏃‍♂️ Ejecutar la Aplicación

### Backend (Puerto 8080)
```bash
cd servicio-calendario
./mvnw spring-boot:run
```
*O ejecutar `ServicioCalendarioApplication.java` desde IntelliJ IDEA*

### Frontend (Puerto 4200)
```bash
cd frontend
ng serve
```
*O usar `ng serve` desde el terminal integrado de VS Code*

## 📖 Cómo Usar la Aplicación

### 1. Acceso Inicial
- Navega a `http://localhost:4200`
- Inicia sesión con tu email

### 2. Crear un Evento
1. **Completa el formulario:**
   - Título del evento
   - Fecha y hora de inicio
   - Fecha y hora de finalización

2. **Agregar invitados (opcional):**
   - Haz clic en "Agregar email"
   - Ingresa los correos de las personas que quieres invitar
   - Puedes agregar múltiples invitados

3. **Crear evento:**
   - Haz clic en "Crear Evento"
   - Si es tu primera vez, se abrirá un popup para autorizar el acceso a Google
   - ¡El evento se creará automáticamente!

### 3. Resultado
- ✅ El evento aparece en tu Google Calendar
- ✅ Se genera automáticamente un enlace de Google Meet
- ✅ Los invitados reciben una invitación por email
- ✅ El evento aparece también en los calendarios de los invitados

## 🔧 API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/authorize?userId={email}` | Obtener URL de autorización de Google |
| `GET` | `/api/callback` | Callback para el proceso OAuth2 |
| `POST` | `/api/createEvent/{userId}` | Crear evento en Google Calendar |

### Ejemplo de Creación de Evento
```json
POST /api/createEvent/tu-email@gmail.com
{
  "summary": "Reunión de Proyecto",
  "startTime": "2024-12-10T10:00:00",
  "endTime": "2024-12-10T11:00:00",
  "emails": ["invitado1@email.com", "invitado2@email.com"]
}
```

## 🗂️ Estructura del Proyecto

```
api-calendar/
├── servicio-calendario/                 # 🔧 Backend Spring Boot
│   ├── src/main/java/.../
│   │   ├── ServicioCalendarioApplication.java
│   │   ├── CalendarController.java      # Controlador principal
│   │   ├── CalendarService.java         # Lógica de negocio
│   │   └── auth/AuthController.java     # Autenticación
│   └── src/main/resources/
│       ├── application.properties       # ⚠️ Configuración (local)
│       └── credentials.json            # ⚠️ Credenciales Google (local)
├── frontend/                           # 🎨 Frontend Angular
│   └── src/app/
│       ├── login/                      # Página de inicio de sesión
│       ├── event-form/                 # Formulario de eventos
│       └── auth.service.ts             # Servicio de autenticación
└── README.md
```

## 🔒 Seguridad y Privacidad

- 🔐 **Autenticación OAuth2** - Acceso seguro a Google Calendar
- 🚫 **Archivos sensibles protegidos** - Credenciales no se suben al repositorio
- ✅ **Plantillas incluidas** - Configuración fácil y segura
- 🔄 **Tokens automáticos** - Renovación automática de permisos

## 🐛 Solución de Problemas

### "Usuario no autenticado"
**Solución:** La app abre automáticamente popup de Google para reautenticación

### "Error de base de datos"
**Solución:**
```bash
mysql -u root -p
CREATE DATABASE caldb;
```

### "Puerto ocupado"
**Solución:** Cambiar puerto en `application.properties` o cerrar la aplicación que usa el puerto

## 👨‍💻 Desarrollado por

**galexxmc** - [GitHub](https://github.com/galexxmc)

---

⭐ **¿Te gustó el proyecto?** ¡Dale una estrella en GitHub!
