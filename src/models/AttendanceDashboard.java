package models;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main Attendance Dashboard Application
 * Provides authentication, role-based views, and attendance management
 */
public class AttendanceDashboard {
    private final Map<String, User> users;
    private final Map<String, Student> students;
    private final Map<String, Teacher> teachers;
    private final Map<String, Course> courses;
    private final AttendanceManager attendanceManager;
    private final Scanner scanner;
    private User currentUser;

    public AttendanceDashboard() {
        this.users = new HashMap<>();
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
        this.attendanceManager = new AttendanceManager("attendance123"); // Default attendance password
        this.scanner = new Scanner(System.in);
        initializeData();
    }

    /**
     * Initialize sample data for demonstration
     */
    private void initializeData() {
        // Create two students
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

        teachers.put(teacher1.getEmployeeId(), teacher1);
        users.put(teacher1.getUsername(), teacher1);

        // Create courses
        Course course1 = new Course("C001", "CS301", "Data Structures & Algorithms",
                "T001", "Mon/Wed 10:00 AM", 4);
        Course course2 = new Course("C002", "CS402", "Machine Learning",
                "T001", "Tue/Thu 2:00 PM", 4);
        Course course3 = new Course("C003", "CS205", "Database Systems",
                "T001", "Mon/Wed/Fri 1:00 PM", 3);

        courses.put(course1.getCourseId(), course1);
        courses.put(course2.getCourseId(), course2);
        courses.put(course3.getCourseId(), course3);

        // Enroll students in courses
        course1.enrollStudent(student1.getStudentId());
        course1.enrollStudent(student2.getStudentId());
        course2.enrollStudent(student1.getStudentId());
        course3.enrollStudent(student2.getStudentId());

        student1.enrollCourse("C001");
        student1.enrollCourse("C002");
        student2.enrollCourse("C001");
        student2.enrollCourse("C003");

        // Assign courses to teacher
        teacher1.assignCourse("C001");
        teacher1.assignCourse("C002");
        teacher1.assignCourse("C003");

        // Add lessons to courses
        course1.addLesson(new Lesson("L001", "C001", "Introduction to Arrays",
                "Basic array operations and complexity",
                LocalDateTime.now().minusDays(7), 90, "Room 201"));
        course1.addLesson(new Lesson("L002", "C001", "Linked Lists",
                "Single and double linked lists",
                LocalDateTime.now().minusDays(5), 90, "Room 201"));
        course1.addLesson(new Lesson("L003", "C001", "Stacks and Queues",
                "Implementation and applications",
                LocalDateTime.now().minusDays(2), 90, "Room 201"));

        course2.addLesson(new Lesson("L004", "C002", "ML Introduction",
                "Overview of machine learning",
                LocalDateTime.now().minusDays(6), 120, "Lab 305"));
        course2.addLesson(new Lesson("L005", "C002", "Linear Regression",
                "Regression models and training",
                LocalDateTime.now().minusDays(3), 120, "Lab 305"));

        // Add quizzes to courses
        course1.addQuiz(new Quiz("Q001", "C001", "Arrays and Complexity Quiz",
                "Test your understanding of arrays",
                LocalDateTime.now().plusDays(3), 20, 30));
        course1.addQuiz(new Quiz("Q002", "C001", "Data Structures Midterm",
                "Comprehensive midterm exam",
                LocalDateTime.now().plusDays(14), 100, 120));

        course2.addQuiz(new Quiz("Q003", "C002", "ML Basics Quiz",
                "Fundamentals of machine learning",
                LocalDateTime.now().plusDays(5), 25, 45));

        // Mark some sample attendance (for student1 only)
        attendanceManager.markAttendance("L001", "S001",
                AttendanceRecord.AttendanceStatus.PRESENT,
                "T001", "attendance123");
        attendanceManager.markAttendance("L002", "S001",
                AttendanceRecord.AttendanceStatus.PRESENT,
                "T001", "attendance123");
        attendanceManager.markAttendance("L004", "S001",
                AttendanceRecord.AttendanceStatus.PRESENT,
                "T001", "attendance123");
    }

    /**
     * Start the dashboard application
     */
    public void start() {
        displayWelcomeBanner();

        if (login()) {
            if (currentUser.isStudent()) {
                studentDashboard();
            } else if (currentUser.isTeacher()) {
                teacherDashboard();
            }
        }

        scanner.close();
    }

    /**
     * Display welcome banner - can be called from Main
     */
    public void displayWelcomeBanner() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║     ATTENDANCE DASHBOARD                         ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");
    }

    /**
     * Display system information - can be called from Main
     */
    public void displaySystemInfo() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            SYSTEM INFORMATION                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Teachers: " + teachers.size());
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Attendance Password: " + attendanceManager.getAttendancePassword());
        System.out.println();
    }

    /**
     * Display demo credentials - can be called from Main
     */
    public void displayDemoCredentials() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            DEMO CREDENTIALS                      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("\nSTUDENT ACCOUNTS:");
        System.out.println("  Username: student1 | Password: pass123");
        System.out.println("  Name: Chiv Inthera (Software Engineering, Year 2)");
        System.out.println("  Enrolled: CS301, CS402");
        System.out.println();
        System.out.println("  Username: student2 | Password: pass123");
        System.out.println("  Name: Song Phengroth (Software Engineering, Year 2)");
        System.out.println("  Enrolled: CS301, CS205");
        System.out.println();
        System.out.println("TEACHER ACCOUNT:");
        System.out.println("  Username: teacher1 | Password: pass123");
        System.out.println("  Name: Mr. Tal Tongsreng");
        System.out.println("  Teaching: CS301, CS402, CS205");
        System.out.println();
        System.out.println("ATTENDANCE PASSWORD: attendance123");
        System.out.println();
    }

    /**
     * Display all courses - can be called from Main
     */
    public void displayAllCourses() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            ALL COURSES                           ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        for (Course course : courses.values()) {
            System.out.println(course);
        }
        System.out.println();
    }

    /**
     * Display all students - can be called from Main
     */
    public void displayAllStudents() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            ALL STUDENTS                          ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        for (Student student : students.values()) {
            System.out.println(student);
        }
        System.out.println();
    }

    /**
     * Display all teachers - can be called from Main
     */
    public void displayAllTeachers() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            ALL TEACHERS                          ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        for (Teacher teacher : teachers.values()) {
            System.out.println(teacher);
        }
        System.out.println();
    }

    /**
     * Display attendance summary - can be called from Main
     */
    public void displayAttendanceSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         ATTENDANCE SUMMARY                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        for (Course course : courses.values()) {
            System.out.println("\n" + course.getCourseCode() + " - " + course.getCourseName());
            System.out.println("───────────────────────────────────────────────────");

            for (String studentId : course.getEnrolledStudentIds()) {
                Student student = students.get(studentId);
                if (student != null) {
                    double percentage = attendanceManager.calculateAttendancePercentage(
                            studentId, course.getLessons());
                    System.out.printf("  %s: %.1f%% (%d/%d lessons)\n",
                            student.getFullName(),
                            percentage,
                            (int)Math.round(percentage * course.getLessons().size() / 100.0),
                            course.getLessons().size());
                }
            }
        }
        System.out.println();
    }

    /**
     * Handle user login with SHA-256 password verification
     */
    private boolean login() {
        System.out.println("═══════════════ LOGIN ═══════════════");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            currentUser = user;
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
            return true;
        } else {
            System.out.println("\nInvalid username or password!");
            System.out.println("\nDemo Credentials:");
            System.out.println("  Student: student1 / pass123");
            System.out.println("  Student: student2 / pass123");
            System.out.println("  Teacher: teacher1 / pass123");
            return false;
        }
    }

    /**
     * Student dashboard - display enrolled courses
     */
    private void studentDashboard() {
        Student student = students.get(((Student) currentUser).getStudentId());

        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║           STUDENT HOME - MY COURSES              ║");
            System.out.println("╚══════════════════════════════════════════════════╝");

            List<Course> enrolledCourses = student.getEnrolledCourseIds().stream()
                    .map(courses::get)
                    .collect(Collectors.toList());

            if (enrolledCourses.isEmpty()) {
                System.out.println("\nYou are not enrolled in any courses.");
                break;
            }

            System.out.println("\nYour Enrolled Courses:");
            for (int i = 0; i < enrolledCourses.size(); i++) {
                Course course = enrolledCourses.get(i);
                double attendance = attendanceManager.calculateAttendancePercentage(
                        student.getStudentId(), course.getLessons());
                System.out.printf("%d. %s | Attendance: %.1f%%\n",
                        i + 1, course, attendance);
            }

            System.out.println("\n0. Logout");
            System.out.print("\nSelect a course (enter number): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            if (choice == 0) {
                System.out.println("\nLogging out...");
                break;
            }

            if (choice > 0 && choice <= enrolledCourses.size()) {
                Course selectedCourse = enrolledCourses.get(choice - 1);
                displayCourseDetails(selectedCourse, student);
            } else {
                System.out.println("Invalid selection!");
            }
        }
    }

    /**
     * Display course details and options (Attendance, Lessons, Quizzes)
     */
    private void displayCourseDetails(Course course, Student student) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║              COURSE DETAILS                      ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.println("Course: " + course.getCourseCode() + " - " + course.getCourseName());
            System.out.println("Schedule: " + course.getSchedule());
            System.out.println("Credits: " + course.getCredits());

            System.out.println("\nSelect an option:");
            System.out.println("1. Attendance");
            System.out.println("2. Lessons");
            System.out.println("3. Quizzes");
            System.out.println("0. Back to courses");

            System.out.print("\nYour choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1:
                    displayAttendanceOptions(course, student);
                    break;
                case 2:
                    displayLessons(course);
                    break;
                case 3:
                    displayQuizzes(course);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    /**
     * Display attendance options and allow marking attendance
     */
    private void displayAttendanceOptions(Course course, Student student) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║              ATTENDANCE MANAGEMENT               ║");
            System.out.println("╚══════════════════════════════════════════════════╝");

            double attendanceRate = attendanceManager.calculateAttendancePercentage(
                    student.getStudentId(), course.getLessons());
            System.out.printf("\nYour Attendance Rate: %.1f%%\n", attendanceRate);

            List<Lesson> lessons = course.getLessons();
            if (lessons.isEmpty()) {
                System.out.println("\nNo lessons available yet.");
                return;
            }

            System.out.println("\nLessons:");
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                AttendanceRecord record = attendanceManager.findAttendanceRecord(
                        lesson.getLessonId(), student.getStudentId());

                String status = record != null ?
                        "✓ " + record.getStatus() : "⊗ Not marked";

                System.out.printf("%d. %s - %s\n", i + 1, lesson, status);
            }

            System.out.println("\nOptions:");
            System.out.println("M. Mark attendance for a lesson");
            System.out.println("0. Back");

            System.out.print("\nYour choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("0")) {
                return;
            } else if (choice.equals("M")) {
                markAttendanceForLesson(lessons, student);
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    /**
     * Mark attendance for a specific lesson
     */
    private void markAttendanceForLesson(List<Lesson> lessons, Student student) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              MARK ATTENDANCE                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        for (int i = 0; i < lessons.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, lessons.get(i).getTitle());
        }

        System.out.print("\nSelect lesson number: ");
        int lessonChoice;
        try {
            lessonChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        if (lessonChoice < 1 || lessonChoice > lessons.size()) {
            System.out.println("Invalid lesson selection!");
            return;
        }

        Lesson selectedLesson = lessons.get(lessonChoice - 1);

        System.out.print("\nEnter attendance password: ");
        String password = scanner.nextLine().trim();

        System.out.println("\nIMPORTANT: Default attendance password is 'attendance123'");

        boolean success = attendanceManager.markAttendance(
                selectedLesson.getLessonId(),
                student.getStudentId(),
                AttendanceRecord.AttendanceStatus.PRESENT,
                currentUser.getUserId(),
                password
        );

        if (success) {
            System.out.println("✓ You have been marked PRESENT for: " + selectedLesson.getTitle());
        }
    }

    /**
     * Display lessons for a course
     */
    private void displayLessons(Course course) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║                  LESSONS                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        List<Lesson> lessons = course.getLessons();
        if (lessons.isEmpty()) {
            System.out.println("\nNo lessons available yet.");
        } else {
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                System.out.printf("\n%d. %s\n", i + 1, lesson.getTitle());
                System.out.println("   Date/Time: " + lesson.getFormattedDateTime());
                System.out.println("   Location: " + lesson.getLocation());
                System.out.println("   Duration: " + lesson.getDuration() + " minutes");
                System.out.println("   Description: " + lesson.getDescription());
            }
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Display quizzes for a course
     */
    private void displayQuizzes(Course course) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║                  QUIZZES                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        List<Quiz> quizzes = course.getQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("\nNo quizzes available yet.");
        } else {
            for (int i = 0; i < quizzes.size(); i++) {
                Quiz quiz = quizzes.get(i);
                System.out.printf("\n%d. %s\n", i + 1, quiz.getTitle());
                System.out.println("   Due Date: " + quiz.getFormattedDueDate());
                System.out.println("   Total Points: " + quiz.getTotalPoints());
                System.out.println("   Duration: " + quiz.getDurationMinutes() + " minutes");
                System.out.println("   Description: " + quiz.getDescription());
            }
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Teacher dashboard - display teaching courses
     */
    private void teacherDashboard() {
        Teacher teacher = teachers.get(((Teacher) currentUser).getEmployeeId());

        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║         TEACHER HOME - TEACHING COURSES          ║");
            System.out.println("╚══════════════════════════════════════════════════╝");

            List<Course> teachingCourses = teacher.getTeachingCourseIds().stream()
                    .map(courses::get)
                    .collect(Collectors.toList());

            if (teachingCourses.isEmpty()) {
                System.out.println("\nYou are not teaching any courses.");
                break;
            }

            System.out.println("\nYour Teaching Courses:");
            for (int i = 0; i < teachingCourses.size(); i++) {
                Course course = teachingCourses.get(i);
                System.out.printf("%d. %s\n", i + 1, course);
            }

            System.out.println("\n0. Logout");
            System.out.print("\nSelect a course (enter number): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            if (choice == 0) {
                System.out.println("\nLogging out...");
                break;
            }

            if (choice > 0 && choice <= teachingCourses.size()) {
                Course selectedCourse = teachingCourses.get(choice - 1);
                displayTeacherCourseDetails(selectedCourse);
            } else {
                System.out.println("Invalid selection!");
            }
        }
    }

    /**
     * Display course management options for teachers
     */
    private void displayTeacherCourseDetails(Course course) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║           COURSE MANAGEMENT                      ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.println("Course: " + course.getCourseCode() + " - " + course.getCourseName());
            System.out.println("Enrolled Students: " + course.getEnrolledStudentCount());

            System.out.println("\nOptions:");
            System.out.println("1. View Students");
            System.out.println("2. View Lessons");
            System.out.println("3. View Attendance Reports");
            System.out.println("0. Back");

            System.out.print("\nYour choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1:
                    displayEnrolledStudents(course);
                    break;
                case 2:
                    displayLessons(course);
                    break;
                case 3:
                    displayAttendanceReports(course);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    /**
     * Display enrolled students
     */
    private void displayEnrolledStudents(Course course) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            ENROLLED STUDENTS                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        List<String> studentIds = course.getEnrolledStudentIds();
        if (studentIds.isEmpty()) {
            System.out.println("\nNo students enrolled yet.");
        } else {
            for (String studentId : studentIds) {
                Student student = students.get(studentId);
                if (student != null) {
                    double attendance = attendanceManager.calculateAttendancePercentage(
                            studentId, course.getLessons());
                    System.out.printf("• %s (ID: %s) - Attendance: %.1f%%\n",
                            student.getFullName(), studentId, attendance);
                }
            }
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Display attendance reports for all lessons
     */
    private void displayAttendanceReports(Course course) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           ATTENDANCE REPORTS                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        List<Lesson> lessons = course.getLessons();
        if (lessons.isEmpty()) {
            System.out.println("\nNo lessons to report on yet.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        for (Lesson lesson : lessons) {
            attendanceManager.displayLessonAttendanceSummary(lesson.getLessonId(), course);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    /**
     * Main method to run the application
     */
    static void main(String[] args) {
        AttendanceDashboard dashboard = new AttendanceDashboard();
        dashboard.start();
    }
}