-- StressLess Database Schema
-- Database: stressless_db
-- Target: MySQL 8.0+ / 9.x

CREATE DATABASE IF NOT EXISTS `stressless_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `stressless_db`;

-- Drop existing tables in reverse dependency order if necessary
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `wellness_goals`;
DROP TABLE IF EXISTS `breathing_sessions`;
DROP TABLE IF EXISTS `user_meditation_completions`;
DROP TABLE IF EXISTS `meditation_sessions`;
DROP TABLE IF EXISTS `journal_entries`;
DROP TABLE IF EXISTS `mood_entries`;
DROP TABLE IF EXISTS `stress_assessments`;
DROP TABLE IF EXISTS `relaxation_resources`;
DROP TABLE IF EXISTS `motivational_quotes`;
DROP TABLE IF EXISTS `users`;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Users Table
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    `avatar_url` VARCHAR(500) NULL,
    `bio` VARCHAR(500) NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Motivational Quotes Table
CREATE TABLE `motivational_quotes` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `quote_text` TEXT NOT NULL,
    `author` VARCHAR(150) NOT NULL DEFAULT 'Unknown',
    `category` VARCHAR(50) DEFAULT 'Mindfulness',
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Stress Assessments Table
CREATE TABLE `stress_assessments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `stress_score` INT NOT NULL,
    `stress_level` ENUM('LOW', 'MODERATE', 'HIGH') NOT NULL,
    `sleep_rating` INT NULL,
    `workload_rating` INT NULL,
    `mood_rating` INT NULL,
    `energy_rating` INT NULL,
    `answers_json` TEXT NULL,
    `recommendations` TEXT NULL,
    `assessed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_stress_user` (`user_id`),
    INDEX `idx_stress_assessed_at` (`assessed_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Mood Entries Table
CREATE TABLE `mood_entries` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `mood` ENUM('HAPPY', 'CALM', 'NEUTRAL', 'ANXIOUS', 'SAD') NOT NULL,
    `energy_level` INT DEFAULT 3, -- 1 to 5 scale
    `notes` TEXT NULL,
    `tags` VARCHAR(255) NULL,
    `logged_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_mood_user` (`user_id`),
    INDEX `idx_mood_logged_at` (`logged_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Journal Entries Table
CREATE TABLE `journal_entries` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `content` LONGTEXT NOT NULL,
    `mood_tag` VARCHAR(50) NULL,
    `tags` VARCHAR(255) NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_journal_user` (`user_id`),
    INDEX `idx_journal_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Guided Meditation Sessions Table
CREATE TABLE `meditation_sessions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT NOT NULL,
    `category` VARCHAR(50) NOT NULL DEFAULT 'Mindfulness', -- Stress, Sleep, Anxiety, Focus, Mindfulness
    `duration_minutes` INT NOT NULL DEFAULT 5,
    `audio_url` VARCHAR(500) NULL,
    `image_url` VARCHAR(500) NULL,
    `difficulty` ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
    `instructions` TEXT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. User Meditation Completions Table
CREATE TABLE `user_meditation_completions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `meditation_session_id` BIGINT NOT NULL,
    `duration_seconds` INT NOT NULL DEFAULT 0,
    `completed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`meditation_session_id`) REFERENCES `meditation_sessions`(`id`) ON DELETE CASCADE,
    INDEX `idx_completion_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Breathing Sessions Table
CREATE TABLE `breathing_sessions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `technique` ENUM('BOX_BREATHING', 'SLOW_PACED', 'FOUR_SEVEN_EIGHT') NOT NULL,
    `duration_seconds` INT NOT NULL,
    `cycles_completed` INT NOT NULL DEFAULT 1,
    `completed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_breathing_user` (`user_id`),
    INDEX `idx_breathing_completed_at` (`completed_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Relaxing Music and Sounds Table
CREATE TABLE `relaxation_resources` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `category` ENUM('RAIN', 'OCEAN', 'FOREST', 'NIGHT', 'WHITE_NOISE', 'MEDITATION_MUSIC', 'PIANO') NOT NULL,
    `audio_url` VARCHAR(500) NOT NULL,
    `icon_name` VARCHAR(50) DEFAULT 'music',
    `description` VARCHAR(255) NULL,
    `duration_seconds` INT DEFAULT 1800,
    `is_featured` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Wellness Goals Table
CREATE TABLE `wellness_goals` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `target_type` ENUM('BREATHING_MINUTES', 'MEDITATION_MINUTES', 'MOOD_CHECKINS', 'JOURNAL_ENTRIES') NOT NULL,
    `target_value` INT NOT NULL DEFAULT 1,
    `current_value` INT NOT NULL DEFAULT 0,
    `period` ENUM('DAILY', 'WEEKLY') NOT NULL DEFAULT 'DAILY',
    `completed` BOOLEAN NOT NULL DEFAULT FALSE,
    `target_date` DATE NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_goals_user` (`user_id`),
    INDEX `idx_goals_target_date` (`target_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
