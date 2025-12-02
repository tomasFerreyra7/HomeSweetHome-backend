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
import org.springframework.web.cors.CorsConfigurationSource;
// Importaciones necesarias para JWT (JwtAuthenticationFilter)

@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Habilita CORS explícitamente
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF (para APIs REST)
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // **CLAVE:** Sesiones sin estado (JWT)
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. AUTENTICACIÓN Y REGISTRO
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // 2. ALOJAMIENTOS (PÚBLICOS PARA VER)
                        // Permitimos ambas escrituras por seguridad (con 1 'm' y con 2 'm')
                        .requestMatchers(HttpMethod.GET, "/api/accomodations").permitAll()       // Lista (1 m)
                        .requestMatchers(HttpMethod.GET, "/api/accomodations/**").permitAll()    // Detalle (1 m)
                        .requestMatchers(HttpMethod.GET, "/api/accommodations").permitAll()      // Lista (2 m)
                        .requestMatchers(HttpMethod.GET, "/api/accommodations/**").permitAll()   // Detalle (2 m)

                        // 3. DISPONIBILIDAD / CALENDARIO (PÚBLICO)
                        // Vital para que el calendario cargue sin estar logueado
                        .requestMatchers(HttpMethod.GET, "/api/reservations/accomodation/**").permitAll()  // (1 m)
                        .requestMatchers(HttpMethod.GET, "/api/reservations/accommodation/**").permitAll() // (2 m)

                        // 4. OTROS PÚBLICOS
                        .requestMatchers(HttpMethod.GET, "/api/amenities").permitAll()
                        .requestMatchers("/error").permitAll()
                        
                        // 5. TODO LO DEMÁS REQUIERE LOGIN
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
