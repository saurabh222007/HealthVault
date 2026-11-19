package com.healthvault.service;

import com.healthvault.model.Doctor;
import com.healthvault.model.User;
import com.healthvault.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DoctorService - Business logic for Doctor operations.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor findByUser(User user) {
        return doctorRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found."));
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public Doctor updateProfile(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
}
