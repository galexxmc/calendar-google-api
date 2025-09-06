package com.oefa.servicio_calendario.auth; // <-- Nuevo paquete

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {


    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AuthUser registerUser(String email, String password) {
        if (authUserRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El correo ya está en uso.");
        }

        AuthUser newUser = new AuthUser();
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordEncoder.encode(password));

        return authUserRepository.save(newUser);
    }

    public Optional<AuthUser> authenticateUser(String email, String password) {
        Optional<AuthUser> user = authUserRepository.findByEmail(email);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPasswordHash())) {
            return user;
        }

        return Optional.empty();
    }
}