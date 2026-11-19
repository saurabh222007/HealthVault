package com.healthvault.service;

import com.healthvault.model.MedicalRecord;
import com.healthvault.model.Patient;
import com.healthvault.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MedicalRecordService - Business logic for health records.
 *
 * Key Java Concepts:
 * - Java Streams API: used to group and filter records functionally
 * - @Slf4j (Lombok): provides a logger without boilerplate
 * - @Transactional: file upload + DB save are atomic
 * - Collectors.groupingBy: groups records by type (like a SQL GROUP BY)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final MedicalRecordRepository recordRepository;

    @Transactional
    public MedicalRecord saveRecord(MedicalRecord record) {
        log.info("Saving medical record for patient id: {}", record.getPatient().getId());
        return recordRepository.save(record);
    }

    public List<MedicalRecord> getRecordsByPatient(Patient patient) {
        return recordRepository.findByPatientOrderByUploadedAtDesc(patient);
    }

    public MedicalRecord findById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found with id: " + id));
    }

    public long countRecordsByPatient(Patient patient) {
        return recordRepository.countByPatient(patient);
    }

    /**
     * Groups records by their type using Java Streams API.
     * Example: { "LAB_REPORT" -> [record1, record2], "PRESCRIPTION" -> [record3] }
     *
     * Key Concept: Collectors.groupingBy() is a terminal stream operation
     * that creates a Map from a list by grouping on a key extractor.
     */
    public Map<MedicalRecord.RecordType, List<MedicalRecord>> getRecordsGroupedByType(Patient patient) {
        List<MedicalRecord> allRecords = getRecordsByPatient(patient);
        return allRecords.stream()
                .collect(Collectors.groupingBy(MedicalRecord::getRecordType));
    }

    @Transactional
    public void deleteRecord(Long id) {
        log.info("Deleting medical record id: {}", id);
        recordRepository.deleteById(id);
    }

    public List<Patient> getPatientsForDoctor(Long doctorId) {
        return recordRepository.findPatientsByDoctorId(doctorId);
    }
}
