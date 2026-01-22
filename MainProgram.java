import java.util.*;

class MainProgram {
    public static void main(String[] args) {
        System.out.println("=== TEACHER SIDE ===\n");
        
        // Initialize teacher service
        TeacherService teacherService = new TeacherService();
        
        // Teacher creates a course
        Course javaCourse = teacherService.createCourse("Java Programming");
        
        // Teacher generates password
        String password = teacherService.generatePassword("Java Programming");
        
        // Teacher generates QR code
        teacherService.generateQR("Java Programming");
        
        // Teacher adds lessons and exercises
        teacherService.addLesson("Java Programming", "Introduction to Java", "Learn basics of Java programming...");
        teacherService.addExercise("Java Programming", "Hello World", "Create your first Java program");
        
        System.out.println("\n=== STUDENT SIDE ===\n");
        
        // Initialize student service (shares same courses map)
        Map<String, Course> sharedCourses = new HashMap<>();
        sharedCourses.put("Java Programming", javaCourse);
        StudentService studentService = new StudentService(sharedCourses);
        
        // Student views available courses
        studentService.viewAvailableCourses();
        
        // Student marks attendance - Test 1: Correct password, Present
        studentService.markAttendance("Java Programming", password, 
                                     "John Doe", "Computer Science", 
                                     AttendanceStatus.PRESENT);
        
        // Student marks attendance - Test 2: Correct password, Late
        studentService.markAttendance("Java Programming", password, 
                                     "Jane Smith", "Software Engineering", 
                                     AttendanceStatus.LATE);
        
        // Student marks attendance - Test 3: Wrong password
        studentService.markAttendance("Java Programming", "WRONG123", 
                                     "Bob Johnson", "IT", 
                                     AttendanceStatus.PRESENT);
        
        System.out.println("\n=== TEACHER VIEWS STUDENT LIST ===");
        
        // Teacher views student list
        teacherService.viewStudentList("Java Programming");
    }
}
