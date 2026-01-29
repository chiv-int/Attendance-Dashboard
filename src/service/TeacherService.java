package service;

import models.Course;

/**
 * TeacherService - Responsibility: Orchestrate teacher-related operations
 */
public class TeacherService {
    private final CourseRepository courseRepository;
    private final PasswordGenerator passwordGenerator;
    private final QRCodeGenerator qrCodeGenerator;
    private final CourseContentService contentService;
    private final AttendanceService attendanceService;
    
    public TeacherService() {
        this.courseRepository = new CourseRepository();
        this.passwordGenerator = new PasswordGenerator();
        this.qrCodeGenerator = new QRCodeGenerator();
        this.contentService = new CourseContentService(courseRepository);
        this.attendanceService = new AttendanceService(courseRepository);
    }
    
    /**
     * Create a new course
     */
    public Course createCourse(String courseName) {
        return courseRepository.createCourse(courseName);
    }
    
    /**
     * Generate attendance password for a course
     */
    public String generatePassword(String courseName) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return null;
        }
        
        String password = passwordGenerator.generateRandomPassword();
        course.setPassword(password);
        System.out.println("Password generated for " + courseName + ": " + password);
        return password;
    }
    
    /**
     * Generate QR code for a course
     */
    public String generateQR(String courseName) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return null;
        }
        
        String password = course.getPassword();
        if (password == null) {
            System.out.println("Error: Generate password first!");
            return null;
        }
        
        return qrCodeGenerator.generateQRData(password);
    }
    
    /**
     * View attendance records (student list)
     */
    public void viewStudentList(String courseName) {
        attendanceService.viewAttendanceRecords(courseName);
    }
    
    /**
     * Add lesson to course
     */
    public void addLesson(String courseName, String title, String content) {
        contentService.addLesson(courseName, title, content);
    }
    
    /**
     * Add exercise to course
     */
    public void addExercise(String courseName, String title, String description) {
        contentService.addExercise(courseName, title, description);
    }

    /**
     * View all lessons for a course
     */
    public void viewLessons(String courseName) {
        contentService.viewLessons(courseName);
    }

    /**
     * View all exercises for a course
     */
    public void viewExercises(String courseName) {
        contentService.viewExercises(courseName);
    }
    
    /**
     * Get course repository for sharing with other services
     */
    public CourseRepository getCourseRepository() {
        return courseRepository;
    }
}
