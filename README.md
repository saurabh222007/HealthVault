# HealthVault Cloud ☁️🏥

> **Integrated Java Programming & Cloud Computing Project**
> Cloud-based Secure Patient Health Records Management System

---

## 📌 Project Overview

HealthVault Cloud is a full-stack web application built with **Java Spring Boot** and deployed on **cloud infrastructure**. It enables patients to securely store and manage their medical records (lab reports, prescriptions, X-rays) in the cloud, while allowing doctors to view their patients' health history.

---

## 🏗️ Tech Stack

| Layer | Technology |
|:---|:---|
| **Backend** | Java 17, Spring Boot 3.2 |
| **Security** | Spring Security (Session-based, BCrypt) |
| **Database ORM** | Spring Data JPA + Hibernate |
| **Frontend** | Thymeleaf + Bootstrap 5 |
| **Cloud Database** | Supabase PostgreSQL |
| **Cloud Storage** | Supabase Storage |
| **Deployment** | Render.com |

---

## 🚀 How to Run (Cloud Deployment)

### Step 1 – Set up Supabase
1. Go to [supabase.com](https://supabase.com) and create a free project
2. Go to **SQL Editor** → paste contents of `schema.sql` → Run
3. Go to **Storage** → Create a bucket named `medical-records` → Make it public
4. Note down your **Project URL** and **Anon Key** from Settings → API
5. Note down your **Database Password** from Settings → Database

### Step 2 – Configure application.properties
Replace the placeholders in `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:postgresql://YOUR_HOST:5432/postgres
spring.datasource.password=YOUR_DB_PASSWORD
supabase.url=https://YOUR_PROJECT.supabase.co
supabase.anon.key=YOUR_ANON_KEY
```

### Step 3 – Deploy to Render.com
1. Push this project to a GitHub repository
2. Go to [render.com](https://render.com) → New Web Service → Connect GitHub repo
3. Set **Build Command**: `mvn clean package -DskipTests`
4. Set **Start Command**: `java -jar target/healthvault-1.0.0.jar`
5. Add Environment Variables (same as application.properties values)
6. Deploy!

---

## 📁 Project Structure

```
src/main/java/com/healthvault/
├── HealthVaultApplication.java   ← Entry point
├── config/SecurityConfig.java    ← Spring Security config
├── controller/                   ← HTTP request handlers
├── model/                        ← JPA Entity classes
├── repository/                   ← Data access layer
├── service/                      ← Business logic
└── dto/                          ← Data Transfer Objects

src/main/resources/
├── templates/                    ← Thymeleaf HTML pages
└── static/                       ← CSS & JavaScript
```

---

## ✨ Features

- 🔐 **Secure Authentication** – Email/password login with BCrypt hashing
- 👤 **Role-Based Access** – Separate Patient and Doctor dashboards
- ☁️ **Cloud File Storage** – Upload medical files to Supabase Storage
- 🗄️ **Cloud Database** – All data stored in PostgreSQL on Supabase
- 📋 **Medical Records** – Upload, categorize, view and delete health records
- 👨‍⚕️ **Doctor View** – Doctors can browse all patients and view their records
- 📱 **Responsive UI** – Works on mobile, tablet, and desktop

---

## 📦 Java Concepts Demonstrated

- OOP (Inheritance, Encapsulation, Polymorphism)
- Spring Boot MVC Architecture
- JPA/Hibernate ORM
- Spring Security & BCrypt
- Java Streams API
- DTO Pattern
- Repository Pattern
- Exception Handling
- @Transactional management
- Bean Validation

---

*Developed as an Integrated Java & Cloud Computing Semester Project*
