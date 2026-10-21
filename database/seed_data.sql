-- =====================================================================
-- Seed Data for Task Management App for Older Adults
-- Sample user: Eleanor Vance (eleanor@eldercare.test / pass123)
-- =====================================================================

USE task_management_db;

-- Insert sample senior user (BCrypt hashed / plain demo password)
INSERT INTO users (id, full_name, email, password, phone_number, emergency_contact_name, emergency_contact_phone)
VALUES (
    1, 
    'Eleanor Vance', 
    'eleanor@eldercare.test', 
    'pass123', 
    '(555) 234-5678', 
    'Sarah Vance (Daughter)', 
    '(555) 876-5432'
) ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- Insert customized senior accessibility preferences
INSERT INTO user_preferences (user_id, font_size, theme, speech_enabled, speech_speed, sound_effects_enabled, simplified_view, high_contrast)
VALUES (
    1, 
    'large', 
    'cream', 
    TRUE, 
    0.85, 
    TRUE, 
    FALSE, 
    FALSE
) ON DUPLICATE KEY UPDATE font_size = VALUES(font_size);

-- Sample tasks for Eleanor
INSERT INTO tasks (user_id, title, description, category, priority, due_date, due_time, reminder_enabled, reminder_minutes_before, is_completed, recurring_pattern, notes)
VALUES 
(
    1,
    'Take Morning Heart Medication (Metoprolol 25mg)',
    'Take 1 tablet with a full glass of water right after breakfast.',
    'MEDICATION',
    'HIGH',
    CURDATE(),
    '08:30:00',
    TRUE,
    15,
    TRUE,
    'DAILY',
    'Prescribed by Dr. Harrison. Stored in blue weekly pill organizer.'
),
(
    1,
    'Dr. Harrison - Annual Vision & Eye Checkup',
    'Appointment at Maplewood Senior Medical Center, Room 204. Daughter Sarah is driving.',
    'APPOINTMENT',
    'HIGH',
    CURDATE(),
    '14:00:00',
    TRUE,
    30,
    FALSE,
    'NONE',
    'Remember to bring reading glasses and current insurance card.'
),
(
    1,
    'Gentle 15-Minute Garden Walk & Stretching',
    'Walk around the backyard patio and do breathing exercises in the warm sun.',
    'HEALTH',
    'MEDIUM',
    CURDATE(),
    '10:30:00',
    TRUE,
    10,
    TRUE,
    'DAILY',
    'Wear supportive walking shoes and take walking stick.'
),
(
    1,
    'Water the Lavender & Porch Petunias',
    'Fill the light green watering can once and gently water the porch planters.',
    'ROUTINE',
    'MEDIUM',
    CURDATE(),
    '16:30:00',
    TRUE,
    15,
    FALSE,
    'DAILY',
    'Check soil moisture before watering.'
),
(
    1,
    'Call Granddaughter Sarah for Birthday Chat',
    'Wish Sarah a happy 18th birthday! Sarah loves talking about her college prep.',
    'SOCIAL',
    'LOW',
    CURDATE(),
    '17:30:00',
    TRUE,
    15,
    FALSE,
    'NONE',
    'Sarah''s cell number: (555) 876-5432.'
),
(
    1,
    'Read 2 Chapters of Mystery Novel in Sunroom',
    'Cozy reading time with chamomile tea.',
    'LEISURE',
    'LOW',
    CURDATE(),
    '19:00:00',
    FALSE,
    0,
    FALSE,
    'NONE',
    'Novel: The Thursday Murder Club.'
);

-- Seed log for usability testing metric tracking
INSERT INTO task_activity_logs (task_id, user_id, action_type, action_details)
VALUES 
(1, 1, 'TASK_COMPLETED', 'Completed Morning Heart Medication at 08:32 AM'),
(3, 1, 'TASK_COMPLETED', 'Completed Gentle 15-Minute Garden Walk at 10:45 AM');
