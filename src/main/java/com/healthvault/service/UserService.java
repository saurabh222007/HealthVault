package com.healthvault.service;

import com.healthvault.dto.RegisterRequest;
import com.healthvault.model.Doctor;
import com.healthvault.model.Patient;
import com.healthvault.model.User;
import com.healthvault.repository.DoctorRepository;
import com.healthvault.repository.PatientRepository;
import com.healthvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService - Handles user registration and profile operations.
 *
 * Key Java Concepts:
 * - @Transactional: Ensures multiple DB operations are atomic (all succeed or all rollback)
 * - PasswordEncoder: Bcrypt hashing — passwords NEVER stored as plain text
 * - Custom Exception: IllegalArgumentException for business rule violations
 * - Java Streams + Optional: for safe null handling
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user (Patient or Doctor).
     * @Transactional ensures if patient/doctor profile save fails, the user is also rolled back.
     */
    @Transactional
    public User registerUser(RegisterRequest request) {
        // Business validation: check if email already in use
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // Business validation: confirm passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // Build User entity from DTO
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        // BCrypt hash the password before storing (NEVER store plain text)
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));

        // Save to database
        User savedUser = userRepository.save(user);

        // Create role-specific profile
        if (savedUser.getRole() == User.Role.PATIENT) {
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patientRepository.save(patient);
        } else if (savedUser.getRole() == User.Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser);
            doctorRepository.save(doctor);
        }

        return savedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}
