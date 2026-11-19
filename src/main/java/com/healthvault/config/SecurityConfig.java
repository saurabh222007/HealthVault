package com.healthvault.config;

import com.healthvault.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SecurityConfig - Configures Spring Security for the application.
 *
 * Key Java Concepts:
 * - @Configuration: Tells Spring this class provides bean definitions
 * - @EnableWebSecurity: Activates Spring Security's web security support
 * - @Bean: Methods annotated with this register Spring-managed objects
 * - SecurityFilterChain: The main security pipeline that inspects every HTTP request
 * - BCryptPasswordEncoder: Industry-standard password hashing algorithm
 * - Role-based Access Control (RBAC): Different URLs accessible by different roles
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Defines URL-level access rules and login/logout behavior.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Public pages (no login required)
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Patient-only pages
                .requestMatchers("/patient/**").hasRole("PATIENT")
                // Doctor-only pages
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                // Admin-only pages
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                     // Custom login page
                .loginProcessingUrl("/login")             // Form POST endpoint
                .usernameParameter("email")               // Field name for email
                .passwordParameter("password")            // Field name for password
                .defaultSuccessUrl("/dashboard", true)    // Redirect after login
                .failureUrl("/login?error=true")          // Redirect on failure
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }

    /**
     * BCrypt password encoder — hashes passwords with a cost factor of 12.
     * BCrypt is a one-way hash: impossible to reverse-engineer the original password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Wires together the UserDetailsService and PasswordEncoder
     * so Spring Security knows how to validate login credentials.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
