package com.healthvault.repository;

import com.healthvault.model.MedicalRecord;
import com.healthvault.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MedicalRecordRepository - Data access for MedicalRecord entities.
 *
 * Key Java Concepts:
 * - @Query: Custom JPQL query when method naming isn't enough
 * - Derived query methods: findByPatient, findByRecordType, etc.
 * - Sorted results using Spring Data's built-in conventions
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    // Find all records for a patient, newest first
    List<MedicalRecord> findByPatientOrderByUploadedAtDesc(Patient patient);

    // Find records by patient and type
    List<MedicalRecord> findByPatientAndRecordTypeOrderByUploadedAtDesc(
            Patient patient, MedicalRecord.RecordType recordType);

    // Count total records for a patient
    long countByPatient(Patient patient);

    // Custom JPQL query - find all patients who have records for a specific doctor
    @Query("SELECT DISTINCT r.patient FROM MedicalRecord r WHERE r.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(Long doctorId);
}
