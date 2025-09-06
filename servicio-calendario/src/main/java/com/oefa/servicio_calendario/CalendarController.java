package com.oefa.servicio_calendario;

import com.oefa.servicio_calendario.CalendarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize(@RequestParam("userId") String userId) throws IOException {
        String authUrl = calendarService.getAuthorizationUrl(userId);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", authUrl).body("Redirigiendo a Google para la autorización.");
    }

    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String code, @RequestParam("state") String userId, HttpServletResponse response) {
        try {
            calendarService.storeCredentials(code, userId);

            // Si atodo es exitoso, envía el script para cerrar la ventana y recargar la página principal
            String script = "<script>window.close(); window.opener.location.reload();</script>";
            response.setContentType("text/html");
            response.getWriter().write(script);
            response.flushBuffer();

        } catch (IOException e) {
            // En caso de error, envía un mensaje de error HTML en la ventana emergente
            e.printStackTrace();
            String errorPage = "<html><body><h1>Error al procesar la autorización.</h1>" +
                    "</body></html>";
            try {
                response.setContentType("text/html");
                response.getWriter().write(errorPage);
            } catch (IOException ex) {
                // Manejar la excepción
            }
        }
    }

    @PostMapping("/createEvent/{userId}")
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody Map<String, Object> eventData, @PathVariable String userId) {
        try {
            Map<String, String> result = calendarService.createEvent(eventData, userId);
            if (result.containsKey("error")) {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno al crear el evento: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}