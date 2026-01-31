package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Course;
import models.Lesson;
import models.Quiz;
import util.DatabaseConnection;

/**
 * Data Access Object for Course management
 * Handles course operations in the database
 */
public class CourseDao {
    private final Connection connection;

    public CourseDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Get course by ID
     */
    public Course getCourseById(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("teacher_id"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                    );
                    
                    // Load enrollments, lessons, and quizzes
                    loadEnrollments(course);
                    loadLessons(course);
                    loadQuizzes(course);
                    
                    return course;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving course!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all courses for a teacher
     */
    public List<Course> getCoursesByTeacher(String teacherId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE teacher_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacherId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("teacher_id"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                    );
                    
                    loadEnrollments(course);
                    loadLessons(course);
                    loadQuizzes(course);
                    
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving courses!");
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Get enrolled courses for a student
     */
    public List<Course> getEnrolledCourses(String studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.* FROM courses c " +
                    "JOIN course_enrollments e ON c.course_id = e.course_id " +
                    "WHERE e.student_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("teacher_id"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                    );
                    
                    loadEnrollments(course);
                    loadLessons(course);
                    loadQuizzes(course);
                    
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving enrolled courses!");
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Enroll a student in a course
     */
    public boolean enrollStudent(String courseId, String studentId) {
        String sql = "INSERT INTO course_enrollments (student_id, course_id) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error enrolling student!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Load enrollments for a course
     */
    private void loadEnrollments(Course course) {
        String sql = "SELECT student_id FROM course_enrollments WHERE course_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    course.enrollStudent(rs.getString("student_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error loading enrollments!");
            e.printStackTrace();
        }
    }

    /**
     * Load lessons for a course
     */
    private void loadLessons(Course course) {
        String sql = "SELECT * FROM lessons WHERE course_id = ? ORDER BY lesson_date";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson(
                        rs.getString("lesson_id"),
                        rs.getString("course_id"),
                        rs.getString("lesson_title"),
                        rs.getString("lesson_description"),
                        rs.getTimestamp("lesson_date").toLocalDateTime(),
                        rs.getInt("duration_minutes"),
                        rs.getString("location")
                    );
                    course.addLesson(lesson);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error loading lessons!");
            e.printStackTrace();
        }
    }

    /**
     * Load quizzes for a course
     */
    private void loadQuizzes(Course course) {
        String sql = "SELECT * FROM quizzes WHERE course_id = ? ORDER BY quiz_date";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Quiz quiz = new Quiz(
                        rs.getString("quiz_id"),
                        rs.getString("course_id"),
                        rs.getString("quiz_title"),
                        rs.getString("quiz_description"),
                        rs.getTimestamp("quiz_date").toLocalDateTime(),
                        rs.getInt("max_score"),
                        rs.getInt("duration_minutes")
                    );
                    course.addQuiz(quiz);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error loading quizzes!");
            e.printStackTrace();
        }
    }

    /**
     * Save a course
     */
    public boolean saveCourse(Course course) {
        String sql = "INSERT INTO courses (course_id, course_code, course_name, teacher_id, schedule, credits) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getCourseName());
            stmt.setString(4, course.getTeacherId());
            stmt.setString(5, course.getSchedule());
            stmt.setInt(6, course.getCredits());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error saving course!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Save a lesson
     */
    public boolean saveLesson(Lesson lesson) {
        String sql = "INSERT INTO lessons (lesson_id, course_id, lesson_title, lesson_description, " +
                    "lesson_date, duration_minutes, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lesson.getLessonId());
            stmt.setString(2, lesson.getCourseId());
            stmt.setString(3, lesson.getTitle());
            stmt.setString(4, lesson.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(lesson.getDateTime()));
            stmt.setInt(6, lesson.getDuration());
            stmt.setString(7, lesson.getLocation());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error saving lesson!");
            e.printStackTrace();
        }
        return false;
    }
}
