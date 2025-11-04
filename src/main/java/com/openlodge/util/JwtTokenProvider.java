package com.openlodge.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// La importación de lombok.extern.slf4j.Slf4j es útil para el log de errores.
// Si no usas Lombok, cambia 'log.error' por un logger estándar de Java.
import lombok.extern.slf4j.Slf4j; 

@Component
@Slf4j // Agrega @Slf4j si usas Lombok
public class JwtTokenProvider {

    // Inyecta los valores desde application.yml
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    // Genera la clave secreta a partir de la cadena en application.yml
    private SecretKey getSigningKey() {
        // Genera la clave a partir del String, idealmente debe ser de al menos 256 bits.
        // Se recomienda usar Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8))
        // en la configuración de la clave para mayor seguridad, pero esta versión funciona si la clave es larga.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Método para generar el token JWT después de una autenticación exitosa
    public String generateToken(Authentication authentication) {
        
        // El principal es el objeto UserDetails retornado por CustomUserDetailsService
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(userPrincipal.getUsername()) // El subject es el email del usuario
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256) // Firma con el algoritmo HS256
                .compact();
    }

    // Método para obtener el email (subject) del token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Método para validar el token JWT
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Firma JWT inválida");
        } catch (MalformedJwtException ex) {
            log.error("Token JWT mal formado");
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado");
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado");
        } catch (IllegalArgumentException ex) {
            log.error("La cadena JWT está vacía");
        }
        return false;
    }
}
