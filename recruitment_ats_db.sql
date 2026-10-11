-- Recruitment and Applicant Tracking System
-- MySQL Database Script
CREATE DATABASE IF NOT EXISTS `recruitment_ats_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `recruitment_ats_db`;

-- Admins Table
CREATE TABLE IF NOT EXISTS `admins` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'ROLE_ADMIN',
    `created_at` DATETIME NOT NULL
);

-- HR Profiles Table
CREATE TABLE IF NOT EXISTS `hr_profiles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `mobile` VARCHAR(20) NOT NULL,
    `company_name` VARCHAR(255) NOT NULL,
    `gender` VARCHAR(20) NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'ROLE_HR',
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL
);

-- Candidates / Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `mobile` VARCHAR(20) NOT NULL,
    `resume_path` VARCHAR(255),
    `resume_original_name` VARCHAR(255),
    `experience` DOUBLE NOT NULL,
    `gender` VARCHAR(20) NOT NULL,
    `age` INT NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL
);

-- Jobs Table
CREATE TABLE IF NOT EXISTS `jobs` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `job_title` VARCHAR(255) NOT NULL,
    `company_name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `required_skills` VARCHAR(255) NOT NULL,
    `experience_required` VARCHAR(255) NOT NULL,
    `qualification` VARCHAR(255) NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    `salary` VARCHAR(255) NOT NULL,
    `job_type` VARCHAR(50) NOT NULL,
    `openings` INT NOT NULL,
    `deadline` DATE NOT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    `hr_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL,
    CONSTRAINT `fk_jobs_hr` FOREIGN KEY (`hr_id`) REFERENCES `hr_profiles` (`id`) ON DELETE CASCADE
);

-- Job Applications Table
CREATE TABLE IF NOT EXISTS `job_applications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `job_id` BIGINT NOT NULL,
    `application_date` DATETIME NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    `notes` VARCHAR(500),
    CONSTRAINT `uc_user_job` UNIQUE (`user_id`, `job_id`),
    CONSTRAINT `fk_app_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_app_job` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE
);

-- Interviews Table
CREATE TABLE IF NOT EXISTS `interviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `application_id` BIGINT NOT NULL,
    `candidate_id` BIGINT NOT NULL,
    `job_id` BIGINT NOT NULL,
    `hr_id` BIGINT NOT NULL,
    `interview_date` DATE NOT NULL,
    `interview_time` VARCHAR(50) NOT NULL,
    `interview_type` VARCHAR(100) NOT NULL,
    `location_or_meeting_link` VARCHAR(500) NOT NULL,
    `notes` VARCHAR(1000),
    `status` VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    `created_at` DATETIME NOT NULL,
    CONSTRAINT `fk_interview_app` FOREIGN KEY (`application_id`) REFERENCES `job_applications` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_interview_candidate` FOREIGN KEY (`candidate_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_interview_job` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_interview_hr` FOREIGN KEY (`hr_id`) REFERENCES `hr_profiles` (`id`) ON DELETE CASCADE
);
