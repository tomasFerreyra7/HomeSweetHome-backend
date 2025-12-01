package com.openlodge.controller;

import com.openlodge.dto.LoginRequest;
import com.openlodge.dto.AuthResponse;
import com.openlodge.entities.User;
import com.openlodge.repository.UserRepository; // Importar si es necesario
import com.openlodge.util.JwtTokenProvider; // (Clase a crear en el paso 3B)

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider; // Clase de utilidad JWT
    private final UserRepository userRepository; // Para obtener más detalles del usuario

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        // 1. Autenticar credenciales
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // 2. Establecer la autenticación y generar JWT
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // 3. Obtener Usuario
        User user = userRepository.findByEmail(loginRequest.getEmail());
                
        // 4. Crear respuesta con el constructor nuevo
        AuthResponse response = new AuthResponse(
            jwt,
            user.getId(),
            user.getEmail(),
            user.getFirstName(),    // Ahora sí enviamos el nombre
            user.getRole().name()   // Enviamos el rol como String (ADMIN, HOST, etc)
        );

        return ResponseEntity.ok(response);
    }
}
