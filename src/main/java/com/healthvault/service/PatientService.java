package com.healthvault.service;

import com.healthvault.model.Patient;
import com.healthvault.model.User;
import com.healthvault.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PatientService - Business logic for Patient operations.
 *
 * Key Java Concepts:
 * - Service Layer Pattern: separates business logic from controller and database layers
 * - @Transactional: Safe multi-step database operations
 * - Optional chaining with orElseThrow for null safety
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient findByUser(User user) {
        return patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found."));
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional
    public Patient updateProfile(Patient patient) {
        return patientRepository.save(patient);
    }
}
