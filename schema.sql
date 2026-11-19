-- =============================================
-- HealthVault Cloud - Database Schema
-- Run this in your Supabase SQL Editor
-- =============================================

-- Users Table (created automatically by Hibernate, but reference here)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('PATIENT', 'DOCTOR', 'ADMIN')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Patients Table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth VARCHAR(20),
    blood_group VARCHAR(5),
    phone VARCHAR(20),
    address TEXT,
    emergency_contact VARCHAR(100),
    allergies TEXT,
    existing_conditions TEXT
);

-- Doctors Table
CREATE TABLE IF NOT EXISTS doctors (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(100),
    license_number VARCHAR(100),
    phone VARCHAR(20),
    hospital_name VARCHAR(200),
    qualifications VARCHAR(200),
    experience_years INT DEFAULT 0
);

-- Medical Records Table
CREATE TABLE IF NOT EXISTS medical_records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id BIGINT REFERENCES doctors(id) ON DELETE SET NULL,
    record_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_name VARCHAR(255),
    file_type VARCHAR(100),
    file_url TEXT,
    cloud_storage_path TEXT,
    uploaded_at TIMESTAMP DEFAULT NOW()
);

-- =============================================
-- Sample Data (optional for testing)
-- =============================================

-- Note: Passwords are BCrypt hashes.
-- The hash below represents the password: "password123"
-- INSERT INTO users (full_name, email, password, role) VALUES
--     ('Test Patient', 'patient@test.com', '$2a$12$...', 'PATIENT'),
--     ('Dr. Test Doctor', 'doctor@test.com', '$2a$12$...', 'DOCTOR');
