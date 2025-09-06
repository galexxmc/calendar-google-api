# Configuración del Proyecto

## Archivos de Configuración

Este proyecto requiere archivos de configuración con credenciales sensibles que no están incluidos en el repositorio por seguridad.

### Pasos para configurar:

1. **Copia el archivo application.properties desde la plantilla:**
   ```bash
   cp servicio-calendario/src/main/resources/application.properties.template servicio-calendario/src/main/resources/application.properties
   ```

2. **Edita application.properties con tus credenciales:**
   - Reemplaza `YOUR_CLIENT_ID_HERE` con tu Client ID de Google OAuth
   - Reemplaza `YOUR_CLIENT_SECRET_HERE` con tu Client Secret de Google OAuth
   - Reemplaza `YOUR_DB_PASSWORD_HERE` con tu contraseña de MySQL

3. **Copia el archivo credentials.json desde la plantilla:**
   ```bash
   cp servicio-calendario/src/main/resources/credentials.json.template servicio-calendario/src/main/resources/credentials.json
   ```

4. **Edita credentials.json con tus credenciales de Google:**
   - Reemplaza `YOUR_CLIENT_ID_HERE` con tu Client ID completo
   - Reemplaza `YOUR_PROJECT_ID_HERE` con tu Project ID de Google Cloud
   - Reemplaza `YOUR_CLIENT_SECRET_HERE` con tu Client Secret
   
   **Alternativa:** Puedes descargar directamente el archivo `credentials.json` desde la consola de Google Cloud (APIs & Services > Credentials)

### Archivos ignorados por Git:
- `application.properties`
- `credentials.json`

Estos archivos están en `.gitignore` para prevenir la exposición accidental de credenciales.
