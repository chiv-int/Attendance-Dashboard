import java.util.*;

public class StudentService {
    private final Map<String, Course> courses;
    
    public StudentService(Map<String, Course> courses) {
        this.courses = courses;
    }
    
    // Mark attendance - student enters password and selects present/late
    public boolean markAttendance(String courseName, String password, 
                                 String studentName, String major, 
                                 AttendanceStatus status) {
        Course course = courses.get(courseName);
        
        if (course == null) {
            System.out.println("Error: Course not found!");
            return false;
        }
        
        if (!course.verifyPassword(password)) {
            System.out.println("Error: Incorrect password!");
            return false;
        }
        
        // Get next record number
        int recordNumber = course.getAttendanceRecords().size() + 1;
        
        // Create attendance record
        AttendanceRecord record = new AttendanceRecord(recordNumber, studentName, major, status);
        course.addAttendanceRecord(record);
        
        System.out.println("Attendance marked successfully!");
        System.out.println("Student: " + studentName + " - Status: " + status);
        return true;
    }
    
    // View available courses
    public void viewAvailableCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        
        System.out.println("\n=== Available Courses ===");
        for (String courseName : courses.keySet()) {
            System.out.println("- " + courseName);
        }
    }
    
    // View lessons for a course
    public void viewLessons(String courseName) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<Lesson> lessons = course.getLessons();
        if (lessons.isEmpty()) {
            System.out.println("No lessons available.");
            return;
        }
        
        System.out.println("\n=== Lessons for " + courseName + " ===");
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            System.out.println((i + 1) + ". " + lesson.getTitle());
            System.out.println("   " + lesson.getContent());
        }
    }
    
    // View exercises for a course
    public void viewExercises(String courseName) {
        Course course = courses.get(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<Exercise> exercises = course.getExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises available.");
            return;
        }
        
        System.out.println("\n=== Exercises for " + courseName + " ===");
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            System.out.println((i + 1) + ". " + exercise.getTitle());
            System.out.println("   " + exercise.getDescription());
        }
    }
}
