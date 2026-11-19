package com.healthvault.service;

import com.healthvault.model.User;
import com.healthvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService - Bridges Spring Security with our User database.
 *
 * Key Java Concepts:
 * - Implements UserDetailsService: Spring Security interface that loads user data during login
 * - @Service: Marks as a Spring-managed service bean
 * - @RequiredArgsConstructor: Lombok injects final fields via constructor (Dependency Injection)
 * - Exception Handling: Throws UsernameNotFoundException if user not found
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Called by Spring Security during every login attempt.
     * Finds user by email (we use email as username).
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException("No user found with email: " + email)
                );
    }
}
