package com.securelogin.service;

import com.securelogin.model.AppUser;
import com.securelogin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("That username is already registered.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("That email is already registered.");
        }

        // Only the BCrypt hash is saved. The original password is never stored.
        String hash = passwordEncoder.encode(rawPassword);
        userRepository.save(new AppUser(username, email, hash));
    }
}
