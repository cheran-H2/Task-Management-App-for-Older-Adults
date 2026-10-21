-- =====================================================================
-- Task Management Application for Older Adults
-- Database Schema (MySQL 8.0+)
-- Designed with accessibility, medication tracking, and clear structure
-- =====================================================================

CREATE DATABASE IF NOT EXISTS task_management_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE task_management_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(30),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. User Preferences (Customized Accessibility Settings)
CREATE TABLE IF NOT EXISTS user_preferences (
    user_id BIGINT PRIMARY KEY,
    font_size VARCHAR(20) NOT NULL DEFAULT 'large',
    theme VARCHAR(30) NOT NULL DEFAULT 'cream',
    speech_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    speech_speed DECIMAL(3,2) NOT NULL DEFAULT 0.90,
    sound_effects_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    simplified_view BOOLEAN NOT NULL DEFAULT FALSE,
    high_contrast BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tasks Table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL DEFAULT 'ROUTINE',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    due_date DATE NOT NULL,
    due_time TIME,
    reminder_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    reminder_minutes_before INT NOT NULL DEFAULT 15,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    recurring_pattern VARCHAR(30) NOT NULL DEFAULT 'NONE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_tasks (user_id, due_date, is_completed),
    INDEX idx_user_priority (user_id, priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Audit / Usability Activity Log (Supports 95% Completion Rate Analytics)
CREATE TABLE IF NOT EXISTS task_activity_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT,
    user_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    action_details TEXT,
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
