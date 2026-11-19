package com.healthvault.controller;

import com.healthvault.model.Doctor;
import com.healthvault.model.MedicalRecord;
import com.healthvault.model.Patient;
import com.healthvault.model.User;
import com.healthvault.service.DoctorService;
import com.healthvault.service.MedicalRecordService;
import com.healthvault.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * DoctorController - Handles all doctor-facing pages.
 *
 * Key Java Concepts:
 * - @RequestMapping("/doctor"): Base URL prefix for all methods in this controller
 * - @PathVariable: Extracts values from the URL path (e.g., /patient/{id})
 * - Service Layer calls: Controller delegates all logic to Service classes
 */
@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MedicalRecordService recordService;

    // ========== DASHBOARD ==========
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User currentUser, Model model) {
        Doctor doctor = doctorService.findByUser(currentUser);
        List<Patient> myPatients = recordService.getPatientsForDoctor(doctor.getId());
        List<Patient> allPatients = patientService.findAllPatients();

        model.addAttribute("doctor", doctor);
        model.addAttribute("myPatients", myPatients);
        model.addAttribute("totalPatients", allPatients.size());
        return "doctor/dashboard";
    }

    // ========== VIEW ALL PATIENTS ==========
    @GetMapping("/patients")
    public String viewAllPatients(@AuthenticationPrincipal User currentUser, Model model) {
        Doctor doctor = doctorService.findByUser(currentUser);
        List<Patient> patients = patientService.findAllPatients();
        model.addAttribute("doctor", doctor);
        model.addAttribute("patients", patients);
        return "doctor/patients";
    }

    // ========== VIEW PATIENT DETAIL + RECORDS ==========
    @GetMapping("/patient/{patientId}")
    public String viewPatientDetail(@PathVariable Long patientId,
                                    @AuthenticationPrincipal User currentUser,
                                    Model model) {
        Doctor doctor = doctorService.findByUser(currentUser);
        Patient patient = patientService.findById(patientId);
        List<MedicalRecord> records = recordService.getRecordsByPatient(patient);

        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", patient);
        model.addAttribute("records", records);
        model.addAttribute("recordTypes", MedicalRecord.RecordType.values());
        return "doctor/patient-detail";
    }

    // ========== DOCTOR PROFILE ==========
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User currentUser, Model model) {
        Doctor doctor = doctorService.findByUser(currentUser);
        model.addAttribute("doctor", doctor);
        return "doctor/profile";
    }

    // ========== UPDATE PROFILE ==========
    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User currentUser,
                                @RequestParam("specialization") String specialization,
                                @RequestParam("licenseNumber") String licenseNumber,
                                @RequestParam("phone") String phone,
                                @RequestParam("hospitalName") String hospitalName,
                                @RequestParam("qualifications") String qualifications,
                                @RequestParam("experienceYears") int experienceYears,
                                RedirectAttributes redirectAttributes) {

        Doctor doctor = doctorService.findByUser(currentUser);
        doctor.setSpecialization(specialization);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setPhone(phone);
        doctor.setHospitalName(hospitalName);
        doctor.setQualifications(qualifications);
        doctor.setExperienceYears(experienceYears);

        doctorService.updateProfile(doctor);
        redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
        return "redirect:/doctor/profile";
    }
}
