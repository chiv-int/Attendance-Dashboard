package gui;

import dao.*;
import java.util.*;
import models.*;


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
        
        // Load data from database
        loadDataFromDatabase();
    }
    
    private void loadDataFromDatabase() {
        try {
            UserDao userDao = new UserDao();
            CourseDao courseDao = new CourseDao();
            
            // Load students from database
            List<Student> dbStudents = userDao.getAllStudents();
            System.out.println("✓ Loaded " + dbStudents.size() + " students from database");
            for (Student student : dbStudents) {
                students.put(student.getStudentId(), student);
                users.put(student.getUsername(), student);
                System.out.println("  - Student: " + student.getUsername() + " (ID: " + student.getStudentId() + ")");
            }
            
            // Load teachers from database
            List<Teacher> dbTeachers = userDao.getAllTeachers();
            System.out.println("✓ Loaded " + dbTeachers.size() + " teachers from database");
            for (Teacher teacher : dbTeachers) {
                teachers.put(teacher.getTeacherId(), teacher);
                users.put(teacher.getUsername(), teacher);
                System.out.println("  - Teacher: " + teacher.getUsername() + " (ID: " + teacher.getTeacherId() + ")");
            }
            
            // Load courses from database
            List<Course> dbCourses = courseDao.getAllCourses();
            System.out.println("✓ Loaded " + dbCourses.size() + " courses from database");
            for (Course course : dbCourses) {
                courses.put(course.getCourseId(), course);
            }
            
            System.out.println("✓ All data loaded from database successfully!");
            System.out.println("  - Students: " + students.size());
            System.out.println("  - Teachers: " + teachers.size());
            System.out.println("  - Courses: " + courses.size());
            System.out.println("  - Total users: " + users.size());
            
        } catch (Exception e) {
            System.err.println("✗ Error loading data from database: " + e.getMessage());
            e.printStackTrace();
            System.err.println("⚠ Warning: Backend initialized with empty data");
        }
    }
    
    // Authentication methods
    public boolean login(String username, String password) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + username);
        System.out.println("Total users in memory: " + users.size());
        System.out.println("Available usernames: " + users.keySet());
        
        User user = users.get(username);
        System.out.println("User found: " + (user != null));
        
        if (user != null) {
            System.out.println("User type: " + user.getClass().getSimpleName());
            boolean passwordMatch = user.verifyPassword(password);
            System.out.println("Password match: " + passwordMatch);
            
            if (passwordMatch) {
                currentUser = user;
                System.out.println("✓ Login successful!");
                return true;
            }
        }
        System.out.println("✗ Login failed!");
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
        // First check database for real attendance record
        if (attendanceDao != null) {
            AttendanceRecord dbRecord = attendanceDao.getAttendanceRecord(courseId, studentId);
            if (dbRecord != null) {
                return dbRecord;
            }
        }
        // Fall back to in-memory if database query fails
        return attendanceManager.findAttendanceRecord(courseId, studentId);
    }
    
    public List<AttendanceRecord> getAllAttendanceForCourse(String courseId) {
        // Get all attendance records from database for this course
        if (attendanceDao != null) {
            return attendanceDao.getAttendanceByCourse(courseId);
        }
        // Fall back to in-memory if database query fails
        return attendanceManager.getCourseAttendance(courseId);
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