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
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) 
            .csrf(AbstractHttpConfigurer::disable) 
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            )
            .authorizeHttpRequests(auth -> auth
                // 1. AUTENTICACIÓN Y REGISTRO
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                // 2. ALOJAMIENTOS (PÚBLICOS PARA VER)
                // Permitimos todas las variantes de escritura comunes
                .requestMatchers(HttpMethod.GET, "/api/accomodations").permitAll()       
                .requestMatchers(HttpMethod.GET, "/api/accomodations/**").permitAll()    
                .requestMatchers(HttpMethod.GET, "/api/accommodations").permitAll()      
                .requestMatchers(HttpMethod.GET, "/api/accommodations/**").permitAll()   

                // 3. DISPONIBILIDAD / CALENDARIO (PÚBLICO) - AQUÍ ESTABA EL ERROR
                // Agregamos singular y plural para asegurar que pase la petición
                .requestMatchers(HttpMethod.GET, "/api/reservations/accomodation/**").permitAll()  
                .requestMatchers(HttpMethod.GET, "/api/reservations/accomodations/**").permitAll() // <--- ESTA ES LA QUE FALLABA
                .requestMatchers(HttpMethod.GET, "/api/reservations/accommodation/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/reservations/accommodations/**").permitAll() 

                // 4. OTROS PÚBLICOS
                .requestMatchers(HttpMethod.GET, "/api/amenities").permitAll()
                .requestMatchers("/error").permitAll()
                
                .anyRequest().authenticated()
            );
    
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}
