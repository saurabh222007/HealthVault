package com.healthvault.controller;

import com.healthvault.model.MedicalRecord;
import com.healthvault.model.Patient;
import com.healthvault.model.User;
import com.healthvault.service.MedicalRecordService;
import com.healthvault.service.PatientService;
import com.healthvault.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * PatientController - Handles all patient-facing pages.
 *
 * Key Java Concepts:
 * - @AuthenticationPrincipal: Injects the currently logged-in User directly
 * - @RequestParam: Binds HTTP form parameters to method arguments
 * - @RequestPart: Binds multipart form file uploads
 * - Model & Model Attributes: Passes data to Thymeleaf templates
 * - Switch Expression (Java 14+): Modern concise switch statement
 */
@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;
    private final MedicalRecordService recordService;
    private final StorageService storageService;

    // ========== DASHBOARD ==========
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User currentUser, Model model) {
        Patient patient = patientService.findByUser(currentUser);
        List<MedicalRecord> recentRecords = recordService.getRecordsByPatient(patient);
        long totalRecords = recordService.countRecordsByPatient(patient);

        // Pass data to the Thymeleaf template
        model.addAttribute("patient", patient);
        model.addAttribute("recentRecords", recentRecords.stream().limit(5).toList());
        model.addAttribute("totalRecords", totalRecords);

        return "patient/dashboard"; // → templates/patient/dashboard.html
    }

    // ========== VIEW ALL RECORDS ==========
    @GetMapping("/records")
    public String viewRecords(@AuthenticationPrincipal User currentUser,
                              @RequestParam(value = "type", required = false) String type,
                              Model model) {

        Patient patient = patientService.findByUser(currentUser);
        List<MedicalRecord> records;

        if (type != null && !type.isEmpty()) {
            // Filter by record type using the enum
            MedicalRecord.RecordType recordType = MedicalRecord.RecordType.valueOf(type.toUpperCase());
            records = recordService.getRecordsByPatient(patient).stream()
                    .filter(r -> r.getRecordType() == recordType)
                    .toList();
            model.addAttribute("selectedType", type);
        } else {
            records = recordService.getRecordsByPatient(patient);
        }

        // Group records by type using Java Streams (for category display)
        Map<MedicalRecord.RecordType, List<MedicalRecord>> groupedRecords =
                recordService.getRecordsGroupedByType(patient);

        model.addAttribute("patient", patient);
        model.addAttribute("records", records);
        model.addAttribute("groupedRecords", groupedRecords);
        model.addAttribute("recordTypes", MedicalRecord.RecordType.values());

        return "patient/records"; // → templates/patient/records.html
    }

    // ========== UPLOAD RECORD PAGE ==========
    @GetMapping("/upload")
    public String uploadPage(@AuthenticationPrincipal User currentUser, Model model) {
        Patient patient = patientService.findByUser(currentUser);
        model.addAttribute("patient", patient);
        model.addAttribute("recordTypes", MedicalRecord.RecordType.values());
        return "patient/upload"; // → templates/patient/upload.html
    }

    // ========== UPLOAD RECORD (POST) ==========
    @PostMapping("/upload")
    public String uploadRecord(@AuthenticationPrincipal User currentUser,
                               @RequestParam("title") String title,
                               @RequestParam("recordType") String recordType,
                               @RequestParam("description") String description,
                               @RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes) {

        try {
            Patient patient = patientService.findByUser(currentUser);

            // Upload file to Supabase Cloud Storage
            String folderPath = storageService.getPatientFolderPath(patient.getId());
            String fileUrl = storageService.uploadFile(file, folderPath);

            // Build MedicalRecord entity and save to PostgreSQL
            MedicalRecord record = new MedicalRecord();
            record.setPatient(patient);
            record.setTitle(title);
            record.setRecordType(MedicalRecord.RecordType.valueOf(recordType));
            record.setDescription(description);
            record.setFileName(file.getOriginalFilename());
            record.setFileType(file.getContentType());
            record.setFileUrl(fileUrl); // Cloud Storage URL saved to DB
            record.setCloudStoragePath(folderPath + "/" + file.getOriginalFilename());

            recordService.saveRecord(record);

            redirectAttributes.addFlashAttribute("successMsg",
                    "Medical record uploaded successfully to cloud storage!");
            return "redirect:/patient/records";

        } catch (IOException e) {
            log.error("File upload failed", e);
            redirectAttributes.addFlashAttribute("errorMsg",
                    "File upload failed: " + e.getMessage());
            return "redirect:/patient/upload";
        }
    }

    // ========== PROFILE PAGE ==========
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User currentUser, Model model) {
        Patient patient = patientService.findByUser(currentUser);
        model.addAttribute("patient", patient);
        return "patient/profile";
    }

    // ========== UPDATE PROFILE ==========
    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User currentUser,
                                @RequestParam("dateOfBirth") String dateOfBirth,
                                @RequestParam("bloodGroup") String bloodGroup,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("emergencyContact") String emergencyContact,
                                @RequestParam("allergies") String allergies,
                                @RequestParam("existingConditions") String existingConditions,
                                RedirectAttributes redirectAttributes) {

        Patient patient = patientService.findByUser(currentUser);
        patient.setDateOfBirth(dateOfBirth);
        patient.setBloodGroup(bloodGroup);
        patient.setPhone(phone);
        patient.setAddress(address);
        patient.setEmergencyContact(emergencyContact);
        patient.setAllergies(allergies);
        patient.setExistingConditions(existingConditions);

        patientService.updateProfile(patient);
        redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
        return "redirect:/patient/profile";
    }

    // ========== DELETE RECORD ==========
    @PostMapping("/records/{id}/delete")
    public String deleteRecord(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        recordService.deleteRecord(id);
        redirectAttributes.addFlashAttribute("successMsg", "Record deleted successfully.");
        return "redirect:/patient/records";
    }
}
