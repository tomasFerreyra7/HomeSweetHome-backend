package com.openlodge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Importaciones necesarias para JWT (JwtAuthenticationFilter)

@Configuration
public class SecurityConfig {

    // Necesitas el CustomUserDetailsService (Paso 2) aquí si no usas el Global AuthenticationManager
    // private final CustomUserDetailsService userDetailsService;

    // public SecurityConfig(CustomUserDetailsService userDetailsService) {
    //     this.userDetailsService = userDetailsService;
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF (para APIs REST)
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // **CLAVE:** Sesiones sin estado (JWT)
                )
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso a la ruta de login
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Permitir acceso a la ruta register
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // Permitir acceso a la ruta de alojamientos por id
                        .requestMatchers(HttpMethod.GET, "/api/accomodations/**").permitAll()

                        // Rutas publicas
                        .requestMatchers("/api/accomodations", "/api/amenities").permitAll()
                        
                        // Proteger las demás rutas (requieren JWT)
                        .anyRequest().authenticated()
                );
        
        // **FALTA:** Añadir el filtro de JWT para validar el token en cada petición (JwtAuthenticationFilter)
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}
