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
('U001', 'intera', 'pass123', 'Chiv Intera', 'STUDENT'),
('U002', 'phengroth', 'pass123', 'Song Phengroth', 'STUDENT'),
('U003', 'sathya', 'pass123', 'Poch Sathya', 'STUDENT'),
('U004', 'kimter', 'pass123', 'Chheng Kimter', 'STUDENT'),
('U005', 'ed_sheeran', 'pass123', 'Ed Sheeran', 'STUDENT'),
('U006', 'dua_lipa', 'pass123', 'Dua Lipa', 'STUDENT'),
('U007', 'the_weeknd', 'pass123', 'The Weeknd', 'STUDENT'),
('U008', 'beyonce', 'pass123', 'Beyoncé', 'STUDENT'),
('U009', 'drake', 'pass123', 'Drake', 'STUDENT'),
('U010', 'travis_scott', 'pass123', 'Travis Scott', 'STUDENT'),
('U011', 'post_malone', 'pass123', 'Post Malone', 'STUDENT'),
('U012', 'billie_eilish', 'pass123', 'Billie Eilish', 'STUDENT'),
('U013', 'tom_cruise', 'pass123', 'Tom Cruise', 'STUDENT'),
('U014', 'tom_hanks', 'pass123', 'Tom Hanks', 'STUDENT'),
('U015', 'leo_dicaprio', 'pass123', 'Leonardo DiCaprio', 'STUDENT'),
('U016', 'chris_evans', 'pass123', 'Chris Evans', 'STUDENT'),
('U017', 'robert_downey', 'pass123', 'Robert Downey Jr.', 'STUDENT'),
('U018', 'johnny_depp', 'pass123', 'Johnny Depp', 'STUDENT'),
('U019', 'will_smith', 'pass123', 'Will Smith', 'STUDENT'),
('U020', 'denzel_washington', 'pass123', 'Denzel Washington', 'STUDENT'),
('U021', 'brad_pitt', 'pass123', 'Brad Pitt', 'STUDENT'),
('U022', 'meryl_streep', 'pass123', 'Meryl Streep', 'STUDENT'),
('U023', 'cate_blanchett', 'pass123', 'Cate Blanchett', 'STUDENT'),
('U024', 'scarlett_johansson', 'pass123', 'Scarlett Johansson', 'STUDENT'),
('U025', 'emma_watson', 'pass123', 'Emma Watson', 'STUDENT'),
('U026', 'emma_stone', 'pass123', 'Emma Stone', 'STUDENT'),
('U027', 'zendaya', 'pass123', 'Zendaya', 'STUDENT'),
('U028', 'timothee_chalamet', 'pass123', 'Timothée Chalamet', 'STUDENT'),
('U029', 'zayn_malik', 'pass123', 'Zayn Malik', 'STUDENT'),
('U030', 'harry_styles', 'pass123', 'Harry Styles', 'STUDENT'),
('U031', 'shakira', 'pass123', 'Shakira', 'STUDENT'),
('U032', 'rihanna', 'pass123', 'Rihanna', 'STUDENT'),
('U033', 'teacher1', 'pass123', 'Dr. Sarah Williams', 'TEACHER');

-- Insert sample students
INSERT INTO students (student_id, user_id, major, year_level) VALUES
('S001', 'U001', 'Software Engineering', 3),
('S002', 'U002', 'Software Engineering', 2),
('S003', 'U003', 'Software Engineering', 2),
('S004', 'U004', 'Software Engineering', 1),
('S005', 'U005', 'Computer Science', 3),
('S006', 'U006', 'Information Technology', 2),
('S007', 'U007', 'Software Engineering', 1),
('S008', 'U008', 'Data Science', 3),
('S009', 'U009', 'Computer Science', 2),
('S010', 'U010', 'Software Engineering', 1),
('S011', 'U011', 'Information Technology', 3),
('S012', 'U012', 'Software Engineering', 2),
('S013', 'U013', 'Computer Science', 3),
('S014', 'U014', 'Data Science', 2),
('S015', 'U015', 'Software Engineering', 1),
('S016', 'U016', 'Information Technology', 3),
('S017', 'U017', 'Computer Science', 2),
('S018', 'U018', 'Software Engineering', 1),
('S019', 'U019', 'Data Science', 3),
('S020', 'U020', 'Computer Science', 2),
('S021', 'U021', 'Software Engineering', 3),
('S022', 'U022', 'Information Technology', 2),
('S023', 'U023', 'Computer Science', 1),
('S024', 'U024', 'Data Science', 3),
('S025', 'U025', 'Software Engineering', 2),
('S026', 'U026', 'Information Technology', 1),
('S027', 'U027', 'Computer Science', 3),
('S028', 'U028', 'Software Engineering', 2),
('S029', 'U029', 'Data Science', 1),
('S030', 'U030', 'Computer Science', 3),
('S031', 'U031', 'Software Engineering', 2),
('S032', 'U032', 'Information Technology', 1);

-- Insert sample teachers
INSERT INTO teachers (teacher_id, user_id, employee_id, department, specialization) VALUES
('T001', 'U033', 'T001', 'Computer Science', 'Data Structures');

-- Insert sample courses
INSERT INTO courses (course_id, course_code, course_name, teacher_id, schedule, credits) VALUES
('C001', 'CS301', 'Data Structures & Algorithms', 'T001', 'Mon/Wed 10:00 AM', 4),
('C002', 'CS402', 'Machine Learning', 'T001', 'Tue/Thu 2:00 PM', 4),
('C003', 'CS205', 'Database Systems', 'T001', 'Mon/Wed/Fri 1:00 PM', 3);

-- Insert sample enrollments
INSERT INTO course_enrollments (student_id, course_id) VALUES
('S001', 'C001'),
('S001', 'C002'),
('S001', 'C003'),
('S002', 'C001'),
('S002', 'C002'),
('S002', 'C003'),
('S003', 'C001'),
('S003', 'C002'),
('S003', 'C003'),
('S004', 'C001'),
('S004', 'C002'),
('S004', 'C003'),
('S005', 'C001'),
('S005', 'C002'),
('S005', 'C003'),
('S006', 'C001'),
('S006', 'C002'),
('S006', 'C003'),
('S007', 'C001'),
('S007', 'C002'),
('S007', 'C003'),
('S008', 'C001'),
('S008', 'C002'),
('S008', 'C003'),
('S009', 'C001'),
('S009', 'C002'),
('S009', 'C003'),
('S010', 'C001'),
('S010', 'C002'),
('S010', 'C003'),
('S011', 'C001'),
('S011', 'C002'),
('S011', 'C003'),
('S012', 'C001'),
('S012', 'C002'),
('S012', 'C003'),
('S013', 'C001'),
('S013', 'C002'),
('S013', 'C003'),
('S014', 'C001'),
('S014', 'C002'),
('S014', 'C003'),
('S015', 'C001'),
('S015', 'C002'),
('S015', 'C003'),
('S016', 'C001'),
('S016', 'C002'),
('S016', 'C003'),
('S017', 'C001'),
('S017', 'C002'),
('S017', 'C003'),
('S018', 'C001'),
('S018', 'C002'),
('S018', 'C003'),
('S019', 'C001'),
('S019', 'C002'),
('S019', 'C003'),
('S020', 'C001'),
('S020', 'C002'),
('S020', 'C003'),
('S021', 'C001'),
('S021', 'C002'),
('S021', 'C003'),
('S022', 'C001'),
('S022', 'C002'),
('S022', 'C003'),
('S023', 'C001'),
('S023', 'C002'),
('S023', 'C003'),
('S024', 'C001'),
('S024', 'C002'),
('S024', 'C003'),
('S025', 'C001'),
('S025', 'C002'),
('S025', 'C003'),
('S026', 'C001'),
('S026', 'C002'),
('S026', 'C003'),
('S027', 'C001'),
('S027', 'C002'),
('S027', 'C003'),
('S028', 'C001'),
('S028', 'C002'),
('S028', 'C003'),
('S029', 'C001'),
('S029', 'C002'),
('S029', 'C003'),
('S030', 'C001'),
('S030', 'C002'),
('S030', 'C003'),
('S031', 'C001'),
('S031', 'C002'),
('S031', 'C003'),
('S032', 'C001'),
('S032', 'C002'),
('S032', 'C003');

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
