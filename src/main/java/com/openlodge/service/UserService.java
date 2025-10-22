package com.openlodge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openlodge.entities.User;
import com.openlodge.repository.UserRepository;
import java.util.List;



@Service
public class UserService {
    
    @Autowired
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    public User partialUpdateUser(Long id, User partialUser) {
        return userRepository.findById(id)
         .map(existingUser -> {
            if (partialUser.getFirstName() != null)
                        existingUser.setFirstName(partialUser.getFirstName());
                    if (partialUser.getLastName() != null)
                        existingUser.setLastName(partialUser.getLastName());
                    if (partialUser.getEmail() != null)
                        existingUser.setEmail(partialUser.getEmail());
                    if (partialUser.getPhone() != null)
                        existingUser.setPhone(partialUser.getPhone());
                    if (partialUser.getCountry() != null)
                        existingUser.setCountry(partialUser.getCountry());
                    if (partialUser.getProvince() != null)
                        existingUser.setProvince(partialUser.getProvince());
                    if (partialUser.getCity() != null)
                        existingUser.setCity(partialUser.getCity());
                    if (partialUser.getBio() != null)
                        existingUser.setBio(partialUser.getBio());
                    if (partialUser.getProfilePictureUrl() != null)
                        existingUser.setProfilePictureUrl(partialUser.getProfilePictureUrl());
                    if (partialUser.getRole() != null)
                        existingUser.setRole(partialUser.getRole());
                    if (partialUser.getIsActive() != null)
                        existingUser.setIsActive(partialUser.getIsActive());
                    if (partialUser.getIsVerified() != null)
                        existingUser.setIsVerified(partialUser.getIsVerified());

                    existingUser.setUpdatedAt(java.time.Instant.now());
                    return userRepository.save(existingUser);
         })
         .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
