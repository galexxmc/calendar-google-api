package com.oefa.servicio_calendario.auth; // <-- Nuevo paquete

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByEmail(String email);
}