package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Student;
import models.Teacher;
import models.User;
import util.DatabaseConnection;

public class UserDao {
    private final Connection connection;

    public UserDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String userId = rs.getString("user_id");
                    
                    if ("STUDENT".equals(role)) {
                        return getStudentById(userId);
                    } else if ("TEACHER".equals(role)) {
                        return getTeacherById(userId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error authenticating user!");
            e.printStackTrace();
        }
        return null;
    }
    public Student getStudentById(String userId) {
        String sql = "SELECT u.*, s.student_id, s.major, s.year_level " +
                    "FROM users u JOIN students s ON u.user_id = s.user_id " +
                    "WHERE u.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),  // Plain text in DB, will be hashed
                        rs.getString("full_name"),
                        rs.getString("student_id"),
                        rs.getString("major"),
                        rs.getInt("year_level")
                    );
                    // Load enrolled courses
                    loadStudentEnrollments(student);
                    return student;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving student!");
            e.printStackTrace();
        }
        return null;
    }
    
    private void loadStudentEnrollments(Student student) {
        String sql = "SELECT course_id FROM course_enrollments WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    student.enrollCourse(rs.getString("course_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error loading student enrollments!");
            e.printStackTrace();
        }
    }
    public Teacher getTeacherById(String userId) {
        String sql = "SELECT u.*, t.teacher_id, t.employee_id, t.department " +
                    "FROM users u JOIN teachers t ON u.user_id = t.user_id " +
                    "WHERE u.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Teacher teacher = new Teacher(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),  // Plain text in DB, will be hashed
                        rs.getString("full_name"),
                        "",  // email - not in database
                        "",  // phone - not in database
                        rs.getString("department")
                    );
                    teacher.setTeacherId(rs.getString("teacher_id"));
                    // Load assigned courses
                    loadTeacherCourses(teacher);
                    return teacher;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving teacher!");
            e.printStackTrace();
        }
        return null;
    }
    
    private void loadTeacherCourses(Teacher teacher) {
        String sql = "SELECT course_id FROM courses WHERE teacher_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getTeacherId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teacher.assignCourse(rs.getString("course_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error loading teacher courses!");
            e.printStackTrace();
        }
    }
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (user_id, username, password, full_name, email, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, "hashed_password"); // Password is already hashed in User model
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.isStudent() ? "STUDENT" : "TEACHER");
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error saving user!");
            e.printStackTrace();
        }
        return false;
    }
    public boolean saveStudent(Student student) {
        try {
            connection.setAutoCommit(false);
            
            // First save user
            if (!saveUser(student)) {
                connection.rollback();
                return false;
            }
            
            // Then save student details
            String sql = "INSERT INTO students (student_id, user_id, major, year_level) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, student.getStudentId());
                stmt.setString(2, student.getUserId());
                stmt.setString(3, student.getProgram());
                stmt.setInt(4, student.getYear());
                
                stmt.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("✗ Error saving student!");
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean saveTeacher(Teacher teacher) {
        try {
            connection.setAutoCommit(false);
            
            // First save user
            if (!saveUser(teacher)) {
                connection.rollback();
                return false;
            }
            
            // Then save teacher details
            String sql = "INSERT INTO teachers (teacher_id, user_id, employee_id, department, specialization) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, teacher.getEmployeeId());
                stmt.setString(2, teacher.getUserId());
                stmt.setString(3, teacher.getEmployeeId());
                stmt.setString(4, teacher.getDepartment());
                stmt.setString(5, ""); // specialization not in model
                
                stmt.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("✗ Error saving teacher!");
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT u.*, s.student_id, s.major, s.year_level " +
                    "FROM users u JOIN students s ON u.user_id = s.user_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),  // Plain text in DB, will be hashed
                    rs.getString("full_name"),
                    rs.getString("student_id"),
                    rs.getString("major"),
                    rs.getInt("year_level")
                );
                loadStudentEnrollments(student);
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving students!");
            e.printStackTrace();
        }
        return students;
    }
    
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT u.*, t.teacher_id, t.employee_id, t.department " +
                    "FROM users u JOIN teachers t ON u.user_id = t.user_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                try {
                    Teacher teacher = new Teacher(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),  // Plain text in DB, will be hashed
                        rs.getString("full_name"),
                        "",  // email - not in database
                        "",  // phone - not in database
                        rs.getString("department")
                    );
                    teacher.setTeacherId(rs.getString("teacher_id"));
                    loadTeacherCourses(teacher);
                    teachers.add(teacher);
                } catch (Exception e) {
                    System.err.println("✗ Error loading teacher: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving teachers!");
            e.printStackTrace();
        }
        return teachers;
    }
}
