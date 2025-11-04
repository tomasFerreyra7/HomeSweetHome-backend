package com.openlodge.service;

import com.openlodge.entities.User;
import com.openlodge.repository.UserRepository;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
// ... otras importaciones
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// ...
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usamos el metodo find by email del repositorio
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con este email: " + email);
        }

        // Retornamos un obbjeto UserDetails de Spring Security
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
