package com.oefa.servicio_calendario;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Permite el acceso a los endpoints de autenticación, autorización y creación de eventos
                        .requestMatchers("/api/auth/**", "/api/authorize", "/api/callback", "/api/createEvent/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Permite tu frontend
        configuration.addAllowedMethod("*"); // Permite todos los métodos (GET, POST, OPTIONS, etc.)
        configuration.addAllowedHeader("*"); // Permite todos los encabezados
        configuration.setAllowCredentials(true); // Permite credenciales como cookies o tokens

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}