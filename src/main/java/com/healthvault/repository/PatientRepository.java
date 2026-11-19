package com.healthvault.repository;

import com.healthvault.model.Patient;
import com.healthvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PatientRepository - Data access for Patient entities.
 * Spring Data JPA generates SQL queries from method names automatically.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserId(Long userId);

    boolean existsByUser(User user);
}
