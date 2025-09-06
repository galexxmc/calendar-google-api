package com.oefa.servicio_calendario.auth; // <-- Nuevo paquete

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        try {
            authService.registerUser(request.get("email"), request.get("password"));
            return ResponseEntity.ok("Usuario registrado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        if (authService.authenticateUser(email, password).isPresent()) {
            return ResponseEntity.ok(email);
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas.");
    }
}