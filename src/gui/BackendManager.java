package gui;

import models.*;
import dao.*;
import java.util.*;

/**
 * Singleton class to bridge GUI and backend
 * Provides centralized access to all backend services
 */
public class BackendManager {
    private static BackendManager instance;
    
    // Core components
    private Map<String, User> users;
    private Map<String, Student> students;
    private Map<String, Teacher> teachers;
    private Map<String, Course> courses;
    private AttendanceManager attendanceManager;
    private AttendanceCodeDao attendanceCodeDao;
    private AttendanceDao attendanceDao;
    
    // Current session
    private User currentUser;
    
    private BackendManager() {
        initializeBackend();
    }
    
    public static BackendManager getInstance() {
        if (instance == null) {
            instance = new BackendManager();
        }
        return instance;
    }
    
    private void initializeBackend() {
        this.users = new HashMap<>();
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
        this.attendanceManager = new AttendanceManager("attendance123");
        
        // Initialize DAOs
        try {
            this.attendanceCodeDao = new AttendanceCodeDao();
            this.attendanceDao = new AttendanceDao();
            System.out.println("✓ Backend initialized with database connection");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            this.attendanceCodeDao = null;
            this.attendanceDao = null;
        }
        
        // Initialize sample data (same as AttendanceDashboard)
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Create students
        Student student1 = new Student("U001", "student1", "pass123",
                "Chiv Intera", "S001", "Software Engineering", 3);
        Student student2 = new Student("U002", "student2", "pass123",
                "Song Phengroth", "S002", "Software Engineering", 2);

        students.put(student1.getStudentId(), student1);
        students.put(student2.getStudentId(), student2);
        users.put(student1.getUsername(), student1);
        users.put(student2.getUsername(), student2);

        // Create teacher
        Teacher teacher1 = new Teacher("U003", "teacher1", "pass123",
                "Dr. Sarah Williams", "T001",
                "Computer Science", "Data Structures");
        teacher1.setTeacherId("T001");

        teachers.put(teacher1.getTeacherId(), teacher1);
        users.put(teacher1.getUsername(), teacher1);

        // Create courses
        Course course1 = new Course("C001", "CS301", "Data Structures & Algorithms",
                "T001", "Mon/Wed 10:00 AM", 4);
        Course course2 = new Course("C002", "CS402", "Machine Learning",
                "T001", "Tue/Thu 2:00 PM", 4);
        Course course3 = new Course("C003", "CS205", "Database Systems",
                "T001", "Mon/Wed/Fri 1:00 PM", 3);
        Course course4 = new Course("C004", "CS101", "Introduction to Software Engineering",
                "T001", "Mon/Wed 9:00 AM", 3);

        courses.put(course1.getCourseId(), course1);
        courses.put(course2.getCourseId(), course2);
        courses.put(course3.getCourseId(), course3);
        courses.put(course4.getCourseId(), course4);

        // Enroll students
        for (Course course : courses.values()) {
            course.enrollStudent(student1.getStudentId());
            course.enrollStudent(student2.getStudentId());
            student1.enrollCourse(course.getCourseId());
            student2.enrollCourse(course.getCourseId());
        }

        // Assign courses to teacher
        for (Course course : courses.values()) {
            teacher1.assignCourse(course.getCourseId());
        }
    }
    
    // Authentication methods
    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public Student getCurrentStudent() {
        if (currentUser instanceof Student) {
            return (Student) currentUser;
        }
        return null;
    }
    
    public Teacher getCurrentTeacher() {
        if (currentUser instanceof Teacher) {
            return (Teacher) currentUser;
        }
        return null;
    }
    
    // Course methods
    public List<Course> getCoursesForCurrentUser() {
        if (currentUser == null) return new ArrayList<>();
        
        if (currentUser.isStudent()) {
            Student student = students.get(((Student) currentUser).getStudentId());
            return student.getEnrolledCourseIds().stream()
                    .map(courses::get)
                    .filter(Objects::nonNull)
                    .collect(java.util.stream.Collectors.toList());
        } else if (currentUser.isTeacher()) {
            Teacher teacher = teachers.get(((Teacher) currentUser).getTeacherId());
            return teacher.getTeachingCourseIds().stream()
                    .map(courses::get)
                    .filter(Objects::nonNull)
                    .collect(java.util.stream.Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }
    
    public Course getCourseByName(String courseName) {
        return courses.values().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst()
                .orElse(null);
    }
    
    // Attendance Code methods
    public String generateAttendanceCode(String courseId, String date, 
                                        String startTime, String endTime) {
        Course course = courses.get(courseId);
        if (course == null) return null;
        
        String password = generateRandomPassword();
        course.setPassword(password);
        course.setAttendanceDateTime(date);
        course.setAttendanceStartTime(startTime);
        course.setAttendanceEndTime(endTime);
        
        // Save to database
        if (attendanceCodeDao != null) {
            try {
                java.time.LocalDate localDate = java.time.LocalDate.parse(date,
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                java.time.LocalTime start = java.time.LocalTime.parse(startTime);
                java.time.LocalTime end = java.time.LocalTime.parse(endTime);
                
                String teacherId = (currentUser instanceof Teacher) 
                    ? ((Teacher)currentUser).getTeacherId() 
                    : "T001";
                
                AttendanceCodeDao.AttendanceCodeRecord record =
                    new AttendanceCodeDao.AttendanceCodeRecord(
                        courseId, password, generateQRCodeData(password),
                        localDate, start, end, teacherId
                    );
                
                attendanceCodeDao.saveAttendanceCode(record);
            } catch (Exception e) {
                System.err.println("Error saving attendance code: " + e.getMessage());
            }
        }
        
        return password;
    }
    
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return password.toString();
    }
    
    private String generateQRCodeData(String password) {
        return "attendance:" + password;
    }
    
    // Attendance marking methods
    public boolean markAttendance(String courseId, String studentId, 
                                 AttendanceRecord.AttendanceStatus status, 
                                 String password) {
        // Validate time window
        Course course = courses.get(courseId);
        if (course == null) return false;
        
        if (!isWithinAttendanceWindow(course)) {
            return false;
        }
        
        String markedBy = currentUser != null ? currentUser.getUserId() : studentId;
        return attendanceManager.markAttendance(courseId, studentId, status, 
                                               markedBy, password);
    }
    
    private boolean isWithinAttendanceWindow(Course course) {
        String startTime = course.getAttendanceStartTime();
        String endTime = course.getAttendanceEndTime();
        
        if (startTime == null || endTime == null) {
            return false;
        }

        java.time.LocalTime currentTime = java.time.LocalTime.now();
        java.time.LocalTime start = java.time.LocalTime.parse(startTime);
        java.time.LocalTime end = java.time.LocalTime.parse(endTime);

        return !currentTime.isBefore(start) && !currentTime.isAfter(end);
    }
    
    // Attendance retrieval methods
    public List<AttendanceRecord> getCourseAttendance(String courseId) {
        return attendanceManager.getCourseAttendance(courseId);
    }
    
    public AttendanceRecord getStudentAttendanceForCourse(String courseId, String studentId) {
        return attendanceManager.findAttendanceRecord(courseId, studentId);
    }
    
    public List<AttendanceRecord> getFilteredAttendance(String courseId,
                                                       java.time.LocalDate fromDate,
                                                       java.time.LocalDate toDate,
                                                       Set<AttendanceRecord.AttendanceStatus> statuses) {
        List<AttendanceRecord> allRecords = attendanceManager.getCourseAttendance(courseId);
        
        return allRecords.stream()
                .filter(record -> {
                    java.time.LocalDate recordDate = record.getTimestamp().toLocalDate();
                    return !recordDate.isBefore(fromDate) && !recordDate.isAfter(toDate);
                })
                .filter(record -> statuses.contains(record.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
    
    public double calculateAttendancePercentage(String studentId, String courseId) {
        Course course = courses.get(courseId);
        if (course == null) return 0.0;
        return attendanceManager.calculateAttendancePercentage(studentId, course.getLessons());
    }
    
    // Getters
    public AttendanceCodeDao getAttendanceCodeDao() {
        return attendanceCodeDao;
    }
    
    public AttendanceDao getAttendanceDao() {
        return attendanceDao;
    }
}