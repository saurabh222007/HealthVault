package com.healthvault.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * StorageService - Handles file uploads to Supabase Cloud Storage.
 *
 * Key Java Concepts:
 * - @Value: Injects values from application.properties into fields
 * - RestTemplate: Java HTTP client for making REST API calls to Supabase
 * - UUID: Generates globally unique file names to prevent overwriting
 * - IOException handling: properly handles file reading errors
 * - Cloud Integration: This is where Java communicates with the Cloud (Supabase Storage API)
 *
 * HOW IT WORKS:
 * 1. User uploads a file through the browser form
 * 2. Spring Boot receives the file as a MultipartFile object
 * 3. This service sends an HTTP PUT request to Supabase Storage REST API
 * 4. Supabase stores the file and returns a public URL
 * 5. We save this URL in our PostgreSQL database (MedicalRecord.fileUrl)
 */
@Service
@Slf4j
public class StorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon.key}")
    private String supabaseAnonKey;

    @Value("${supabase.storage.bucket}")
    private String bucketName;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Uploads a file to Supabase Storage and returns the public URL.
     *
     * @param file       The file uploaded from the browser
     * @param folderPath The folder path inside the bucket (e.g., "patient-123")
     * @return Public URL of the uploaded file
     */
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        // Generate a unique filename to avoid overwriting existing files
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String objectPath = folderPath + "/" + uniqueFileName;

        // Build the Supabase Storage API endpoint URL
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + objectPath;

        // Set HTTP headers for the Supabase API request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseAnonKey);
        headers.setContentType(MediaType.parseMediaType(
                file.getContentType() != null ? file.getContentType() : "application/octet-stream"
        ));

        // Create HTTP request with file bytes as body
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        log.info("Uploading file to Supabase: {}", objectPath);

        // Make the HTTP POST request to Supabase Storage API
        ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl, HttpMethod.POST, requestEntity, String.class
        );

        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            // Build and return the public URL of the uploaded file
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + objectPath;
            log.info("File uploaded successfully. Public URL: {}", publicUrl);
            return publicUrl;
        } else {
            throw new RuntimeException("File upload failed. Status: " + response.getStatusCode());
        }
    }

    /**
     * Returns the cloud storage path for a given patient.
     */
    public String getPatientFolderPath(Long patientId) {
        return "patient-" + patientId;
    }

    /**
     * Deletes a file from Supabase Storage by its path.
     */
    public void deleteFile(String objectPath) {
        String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + objectPath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseAnonKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);
        log.info("File deleted from cloud storage: {}", objectPath);
    }
}
