package com.healthvault.repository;

import com.healthvault.model.Doctor;
import com.healthvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DoctorRepository - Data access for Doctor entities.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUser(User user);

    Optional<Doctor> findByUserId(Long userId);

    boolean existsByUser(User user);
}
