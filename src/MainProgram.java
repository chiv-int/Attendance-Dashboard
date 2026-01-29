import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import service.*;

/**
 * MainProgram - Entry point for the Attendance Dashboard
 */
public class MainProgram {
    private static TeacherService teacherService;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        teacherService = new TeacherService();
        seedDefaultCourses();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    showTeacherLoginFlow();
                    break;
                case "0":
                    System.out.println("Thank you for using Attendance Dashboard. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== ATTENDANCE DASHBOARD =====");
        System.out.println("1. Teacher Login");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    
    private static void showTeacherLoginFlow() {
        System.out.println("\n===== TEACHER LOGIN =====");
        System.out.print("Enter teacher name: ");
        String teacherName = scanner.nextLine().trim();
        if (teacherName.isEmpty()) {
            System.out.println("Teacher name is required.");
            return;
        }
        System.out.println("Welcome, " + teacherName + "!");
        
        boolean selectingCourses = true;
        while (selectingCourses) {
            String courseName = selectCourseFromList();
            if (courseName == null) {
                selectingCourses = false;
                continue;
            }
            showCourseMenu(courseName);
        }
    }
    
    private static void showCourseMenu(String courseName) {
        boolean inCourseMenu = true;
        while (inCourseMenu) {
            System.out.println("\n===== " + courseName + " =====");
            System.out.println("1. Attendance");
            System.out.println("2. Lessons");
            System.out.println("3. Exercises");
            System.out.println("0. Back to Course List");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleAttendanceFlow(courseName);
                    break;
                case "2":
                    teacherService.viewLessons(courseName);
                    break;
                case "3":
                    teacherService.viewExercises(courseName);
                    break;
                case "0":
                    inCourseMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void handleAttendanceFlow(String courseName) {
        System.out.println("\n=== ATTENDANCE TOOLS ===");
        String password = teacherService.generatePassword(courseName);
        if (password != null) {
            System.out.println("\n*** PASSWORD GENERATED: " + password + " ***");
            teacherService.generateQR(courseName);
        }
    }
    
    private static String selectCourseFromList() {
        List<String> courses = getCourseNames();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return null;
        }
        
        System.out.println("\nSelect a class:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        System.out.println("0. Logout");
        System.out.print("Choose a class: ");
        
        String choice = scanner.nextLine().trim();
        if ("0".equals(choice)) {
            return null;
        }
        
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < courses.size()) {
                return courses.get(index);
            }
        } catch (NumberFormatException ignored) {
        }
        
        System.out.println("Invalid selection. Please try again.");
        return selectCourseFromList();
    }
    
    private static List<String> getCourseNames() {
        Map<String, models.Course> courseMap = teacherService.getCourseRepository().getAllCourses();
        return new ArrayList<>(courseMap.keySet());
    }
    
    private static void seedDefaultCourses() {
        ensureCourseExists("Software Engineering");
        ensureCourseExists("Computer Science");
        ensureCourseExists("Cyber Security and Ai");
    }
    
    private static void ensureCourseExists(String courseName) {
        if (!teacherService.getCourseRepository().courseExists(courseName)) {
            teacherService.createCourse(courseName);
        }
    }
}
