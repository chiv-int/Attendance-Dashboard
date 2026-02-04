
import java.util.*;
import models.*;

public class TeacherService {
    private final Map<String, Course> courses;
    
    public TeacherService() {
        this.courses = new HashMap<>();
    }
    
    // Create a new course
    public Course createCourse(String courseName) {
        Course course = new Course(courseName, null);
        courses.put(courseName, course);
        System.out.println("Course created: " + courseName);
        return course;
    }
    
    // Generate password for attendance (gen-pass)
    public String generatePassword(String courseName) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return null;
        }
        
        // Generate random 6-character password
        String password = generateRandomPassword();
        course.setPassword(password);
        System.out.println("Password generated for " + courseName + ": " + password);
        return password;
    }
    
    // Generate QR code (for now just returns the password)
    public String generateQR(String courseName) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return null;
        }
        
        String password = course.getPassword();
        if (password == null) {
            System.out.println("Error: Generate password first!");
            return null;
        }
        
        System.out.println("QR Code generated for " + courseName);
        System.out.println("QR Data: " + password);
        return password;
    }
    
    // View student list (attendance records)
    public void viewStudentList(String courseName) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<AttendanceRecord> records = course.getAttendanceRecords();
        if (records.isEmpty()) {
            System.out.println("No attendance records yet.");
            return;
        }
        
        System.out.println("\n=== Student List for " + courseName + " ===");
        System.out.printf("%-3s %-20s %-15s %-20s %-10s\n", "No", "Name", "Major", "Date", "Status");
        System.out.println("=".repeat(75));
        
        for (AttendanceRecord record : records) {
            System.out.println(record);
        }
    }
    
    // Add lesson to course
    public void addLesson(String courseName, String title, String content) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        Lesson lesson = new Lesson(title, content);
        course.addLesson(lesson);
        System.out.println("Lesson added: " + title);
    }
    
    // Add exercise to course
    public void addExercise(String courseName, String title, String description) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        Exercise exercise = new Exercise(title, description);
        course.addExercise(exercise);
        System.out.println("Exercise added: " + title);
    }
    
    // Get course
    public Course getCourse(String courseName) {
        return courses.get(courseName);
    }
    
    // Helper method to generate random password
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}

