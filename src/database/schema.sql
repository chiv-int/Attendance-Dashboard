-- ============================================
-- Attendance Dashboard Database Schema
-- MySQL Database
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS attendance_dashboard;
USE attendance_dashboard;

-- ============================================
-- Table: users
-- Stores basic user authentication info
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    role ENUM('STUDENT', 'TEACHER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: students
-- Stores student-specific information
-- ============================================
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    major VARCHAR(200),
    year_level INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: teachers
-- Stores teacher-specific information
-- ============================================
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    employee_id VARCHAR(50) UNIQUE NOT NULL,
    department VARCHAR(200),
    specialization VARCHAR(200),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_employee_id (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: courses
-- Stores course information
-- ============================================
CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(50) PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL,
    course_name VARCHAR(200) NOT NULL,
    teacher_id VARCHAR(50) NOT NULL,
    schedule VARCHAR(200),
    credits INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_course_code (course_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: course_enrollments
-- Stores student enrollments in courses
-- ============================================
CREATE TABLE IF NOT EXISTS course_enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(50) NOT NULL,
    course_id VARCHAR(50) NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: lessons
-- Stores lesson/class session information
-- ============================================
CREATE TABLE IF NOT EXISTS lessons (
    lesson_id VARCHAR(50) PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL,
    lesson_title VARCHAR(200) NOT NULL,
    lesson_description TEXT,
    lesson_date DATETIME NOT NULL,
    duration_minutes INT,
    location VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_lesson_date (lesson_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: attendance_codes
-- Stores generated attendance codes with time windows
-- ============================================
CREATE TABLE IF NOT EXISTS attendance_codes (
    code_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    qr_code_data VARCHAR(200),
    attendance_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_attendance_date (attendance_date),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: attendance_records
-- Stores individual attendance records
-- ============================================
CREATE TABLE IF NOT EXISTS attendance_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id VARCHAR(50) NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    status ENUM('PRESENT', 'ABSENT', 'LATE', 'EXCUSED') NOT NULL,
    marked_by VARCHAR(50),
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    code_id INT,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (code_id) REFERENCES attendance_codes(code_id) ON DELETE SET NULL,
    UNIQUE KEY unique_attendance (course_id, student_id),
    INDEX idx_course_id (course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_status (status),
    INDEX idx_marked_at (marked_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: quizzes
-- Stores quiz information
-- ============================================
CREATE TABLE IF NOT EXISTS quizzes (
    quiz_id VARCHAR(50) PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL,
    quiz_title VARCHAR(200) NOT NULL,
    quiz_description TEXT,
    quiz_date DATETIME NOT NULL,
    max_score INT,
    duration_minutes INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_quiz_date (quiz_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: exercises
-- Stores exercise/assignment information
-- ============================================
CREATE TABLE IF NOT EXISTS exercises (
    exercise_id VARCHAR(50) PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL,
    exercise_title VARCHAR(200) NOT NULL,
    exercise_description TEXT,
    due_date DATETIME NOT NULL,
    max_score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Insert sample data (for testing)
-- ============================================

-- Insert sample users
INSERT INTO users (user_id, username, password, full_name, role) VALUES
('U001', 'student1', 'pass123', 'Chiv Intera', 'STUDENT'),
('U002', 'student2', 'pass123', 'Song Phengroth', 'STUDENT'),
('U003', 'teacher1', 'pass123', 'Dr. Sarah Williams', 'TEACHER');

-- Insert sample students
INSERT INTO students (student_id, user_id, major, year_level) VALUES
('S001', 'U001', 'Software Engineering', 3),
('S002', 'U002', 'Software Engineering', 2);

-- Insert sample teachers
INSERT INTO teachers (teacher_id, user_id, employee_id, department, specialization) VALUES
('T001', 'U003', 'T001', 'Computer Science', 'Data Structures');

-- Insert sample courses
INSERT INTO courses (course_id, course_code, course_name, teacher_id, schedule, credits) VALUES
('C001', 'CS301', 'Data Structures & Algorithms', 'T001', 'Mon/Wed 10:00 AM', 4),
('C002', 'CS402', 'Machine Learning', 'T001', 'Tue/Thu 2:00 PM', 4),
('C003', 'CS205', 'Database Systems', 'T001', 'Mon/Wed/Fri 1:00 PM', 3);

-- Insert sample enrollments
INSERT INTO course_enrollments (student_id, course_id) VALUES
('S001', 'C001'),
('S001', 'C002'),
('S002', 'C001'),
('S002', 'C003');

-- Insert sample lessons
INSERT INTO lessons (lesson_id, course_id, lesson_title, lesson_description, lesson_date, duration_minutes, location) VALUES
('L001', 'C001', 'Introduction to Arrays', 'Basic array operations and complexity', '2026-01-24 14:31:00', 90, 'Room 201'),
('L002', 'C001', 'Linked Lists', 'Single and double linked lists', '2026-01-26 14:31:00', 90, 'Room 201'),
('L003', 'C001', 'Stacks and Queues', 'Implementation and applications', '2026-01-29 14:31:00', 90, 'Room 201'),
('L004', 'C002', 'ML Introduction', 'Overview of machine learning', '2026-01-25 14:31:00', 120, 'Lab 305'),
('L005', 'C002', 'Linear Regression', 'Regression models and training', '2026-01-28 14:31:00', 120, 'Lab 305');

-- Insert sample quizzes
INSERT INTO quizzes (quiz_id, course_id, quiz_title, quiz_description, quiz_date, max_score, duration_minutes) VALUES
('Q001', 'C001', 'Arrays and Complexity Quiz', 'Test your understanding of arrays', '2026-02-03 10:00:00', 20, 30),
('Q002', 'C001', 'Data Structures Midterm', 'Comprehensive midterm exam', '2026-02-14 10:00:00', 100, 120),
('Q003', 'C002', 'ML Basics Quiz', 'Fundamentals of machine learning', '2026-02-05 14:00:00', 25, 45);
