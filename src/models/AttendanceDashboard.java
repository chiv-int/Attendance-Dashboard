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
    private dao.AttendanceCodeDao attendanceCodeDao;
    private dao.AttendanceDao attendanceDao;

    public AttendanceDashboard() {
        this.users = new HashMap<>();
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
        this.attendanceManager = new AttendanceManager("attendance123");
        this.scanner = new Scanner(System.in);
        
        // Initialize database DAOs
        try {
            this.attendanceCodeDao = new dao.AttendanceCodeDao();
            this.attendanceDao = new dao.AttendanceDao();
            System.out.println("✓ Database connection initialized");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            this.attendanceCodeDao = null;
            this.attendanceDao = null;
        }
        
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
        teacher1.setTeacherId("T001");  // Set the actual teacher ID

        teachers.put(teacher1.getTeacherId(), teacher1);
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
        course2.enrollStudent(student2.getStudentId());
        course3.enrollStudent(student1.getStudentId());
        course3.enrollStudent(student2.getStudentId());

        student1.enrollCourse("C001");
        student1.enrollCourse("C002");
        student1.enrollCourse("C003");
        student2.enrollCourse("C001");
        student2.enrollCourse("C002");
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
        attendanceManager.markAttendance("C001", "S001",
                AttendanceRecord.AttendanceStatus.PRESENT,
                "T001", "attendance123");
        attendanceManager.markAttendance("C002", "S001",
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
/**
 * Display attendance options and allow marking attendance
 */
private void displayAttendanceOptions(Course course, Student student) {
    while (true) {
        // Load attendance code from database if not set in memory
        if (course.getAttendanceStartTime() == null && attendanceCodeDao != null) {
            try {
                dao.AttendanceCodeDao.AttendanceCodeRecord codeRecord = 
                    attendanceCodeDao.getActiveCodeForCourse(course.getCourseId());
                
                if (codeRecord != null) {
                    course.setPassword(codeRecord.getPassword());
                    course.setAttendanceDateTime(codeRecord.getAttendanceDate().toString());
                    course.setAttendanceStartTime(codeRecord.getStartTime().toString());
                    course.setAttendanceEndTime(codeRecord.getEndTime().toString());
                    
                    // DEBUG: Show loaded password
                    System.out.println("✓ Loaded password from database: " + codeRecord.getPassword());
                    System.out.println("[DEBUG] Course object ID: " + System.identityHashCode(course));
                    System.out.println("[DEBUG] Course password after load: " + course.getPassword());
                }
            } catch (Exception e) {
                System.err.println("⚠ Warning: Could not load attendance code from database");
                e.printStackTrace();
            }
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              ATTENDANCE MANAGEMENT               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        double attendanceRate = attendanceManager.calculateAttendancePercentage(
                student.getStudentId(), course.getLessons());
        System.out.printf("\nYour Attendance Rate: %.1f%%\n", attendanceRate);

        System.out.println("\nAttendance Window Status:");
        String windowStatus = course.getAttendanceStartTime() != null ? 
            "Date: " + course.getAttendanceDateTime() + " | Time: " + course.getAttendanceStartTime() + " - " + course.getAttendanceEndTime() :
            "No attendance code generated yet";
        System.out.println(windowStatus);

        System.out.println("\nOptions:");
        System.out.println("M. Mark attendance for this course");
        System.out.println("0. Back");

        System.out.print("\nYour choice: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("0")) {
            return;
        } else if (choice.equals("M")) {
            // Check if attendance code has been generated
            if (course.getAttendanceStartTime() == null || course.getAttendanceEndTime() == null) {
                System.out.println("\n✗ Teacher has not generated an attendance code yet.");
                System.out.println("Please wait for the teacher to generate the attendance code.");
            } else {
                // Mark attendance for the course
                markAttendanceForCourse(course, student);
            }
        } else {
            System.out.println("Invalid choice!");
        }
    }
}

/**
 * Mark attendance for a course (simplified - no lesson selection)
 */
private void markAttendanceForCourse(Course course, Student student) {
    System.out.println("\n╔══════════════════════════════════════════════════╗");
    System.out.println("║              MARK ATTENDANCE                     ║");
    System.out.println("╚══════════════════════════════════════════════════╝");

    System.out.println("\nCourse: " + course.getCourseCode() + " - " + course.getCourseName());
    System.out.println("Date: " + course.getAttendanceDateTime());
    System.out.println("Time Window: " + course.getAttendanceStartTime() + " - " + course.getAttendanceEndTime());
    
    // DEBUG: Show what student data we have
    System.out.println("[DEBUG] Student object: " + student);
    System.out.println("[DEBUG] Student ID: " + student.getStudentId());
    System.out.println("[DEBUG] CurrentUser ID: " + currentUser.getUserId());
    System.out.println("[DEBUG] CurrentUser full name: " + currentUser.getFullName());
    
    // Check if current time is within attendance window - MUST be within window
    if (!isWithinAttendanceWindow(course)) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
        return;
    }

    System.out.print("\nEnter attendance password: ");
    String password = scanner.nextLine().trim();

    // Mark attendance for the course
    boolean success = attendanceManager.markAttendance(
            course.getCourseId(),
            student.getStudentId(),
            AttendanceRecord.AttendanceStatus.PRESENT,
            currentUser.getUserId(),
            attendanceManager.getAttendancePassword()
    );

    if (success) {
        System.out.println("\n✓ Attendance marked successfully for " + course.getCourseName());
        System.out.println("✓ Date: " + course.getAttendanceDateTime());
    } else {
        System.out.println("\n✗ Failed to mark attendance. You may have already marked attendance.");
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
        Teacher teacher = teachers.get(((Teacher) currentUser).getTeacherId());

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
            System.out.println("1. Attendance");
            System.out.println("2. View Lessons");
            System.out.println("3. View Exercises");
            System.out.println("4. Quiz");
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
                    displayAttendanceMenu(course);
                    break;
                case 2:
                    displayLessons(course);
                    break;
                case 3:
                    System.out.println("\n╔══════════════════════════════════════════════════╗");
                    System.out.println("║                 EXERCISES                        ║");
                    System.out.println("╚══════════════════════════════════════════════════╝");
                    System.out.println("\nExercises management coming soon!");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 4:
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
     * Display attendance submenu for teachers
     */
    private void displayAttendanceMenu(Course course) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║            ATTENDANCE TOOLS                      ║");
            System.out.println("╚══════════════════════════════════════════════════╝");

            System.out.println("\nOptions:");
            System.out.println("1. Generate Code");
            System.out.println("2. View Student");
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
                    generateAttendanceCode(course);
                    break;
                case 2:
                    autoMarkAbsentIfTimeEnded(course);
                    displayEnrolledStudents(course);
                    break;
                case 3:
                    autoMarkAbsentIfTimeEnded(course);
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
     * Generate attendance code for a course with specific date and time
     */
    private void generateAttendanceCode(Course course) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          GENERATE ATTENDANCE CODE                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        String attendanceDate = "";
        String startTime = "";
        String endTime = "";
        boolean validInput = false;

        // Validate attendance date
        while (!validInput) {
            System.out.print("\nEnter attendance date (DD/MM/YYYY): ");
            attendanceDate = scanner.nextLine().trim();
            if (isValidDate(attendanceDate)) {
                validInput = true;
            } else {
                System.out.println("✗ Invalid date format! Please enter date in DD/MM/YYYY format.");
            }
        }

        // Validate start time
        validInput = false;
        while (!validInput) {
            System.out.print("Enter start time (HH:MM): ");
            startTime = scanner.nextLine().trim();
            if (isValidTime(startTime)) {
                validInput = true;
            } else {
                System.out.println("✗ Invalid time format! Please enter time in HH:MM format (00:00 - 23:59).");
            }
        }

        // Validate end time
        validInput = false;
        while (!validInput) {
            System.out.print("Enter end time (HH:MM): ");
            endTime = scanner.nextLine().trim();
            if (isValidTime(endTime)) {
                validInput = true;
            } else {
                System.out.println("✗ Invalid time format! Please enter time in HH:MM format (00:00 - 23:59).");
            }
        }

        String password = generateRandomPassword();
        course.setPassword(password);
        course.setAttendanceDateTime(attendanceDate);
        course.setAttendanceStartTime(startTime);
        course.setAttendanceEndTime(endTime);

        // Save to database
        if (attendanceCodeDao != null) {
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(attendanceDate, 
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                java.time.LocalTime start = java.time.LocalTime.parse(startTime);
                java.time.LocalTime end = java.time.LocalTime.parse(endTime);
                
                // Get teacher ID for the current user
                String teacherId = (currentUser instanceof Teacher) 
                    ? ((Teacher)currentUser).getTeacherId() 
                    : "T001"; // Default fallback
                
                dao.AttendanceCodeDao.AttendanceCodeRecord record = 
                    new dao.AttendanceCodeDao.AttendanceCodeRecord(
                        course.getCourseId(),
                        password,
                        generateQRCodeData(password),
                        date,
                        start,
                        end,
                        teacherId
                    );
                
                boolean saved = attendanceCodeDao.saveAttendanceCode(record);
                
                System.out.println("\n✓ Attendance Code Generated Successfully!");
                if (saved) {
                    System.out.println("✓ Code saved to database successfully!");
                } else {
                    System.err.println("⚠ Warning: Code was not saved to database");
                }
            } catch (Exception e) {
                System.out.println("\n✓ Attendance Code Generated Successfully!");
                System.err.println("✗ ERROR: Could not save to database!");
                System.err.println("Error details: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("\n✓ Attendance Code Generated Successfully!");
            System.err.println("✗ Database connection is NULL");
        }

        System.out.println("\n*** ATTENDANCE DETAILS ***");
        System.out.println("Date: " + attendanceDate);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("\n*** PASSWORD: " + password + " ***");
        System.out.println("*** QR CODE DATA: " + generateQRCodeData(password) + " ***");
        System.out.println("\nShare this code with students for attendance marking.");
        System.out.println("Students can only mark attendance between " + startTime + " and " + endTime);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Generate random password for attendance
     */
    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return password.toString();
    }

    /**
     * Generate QR code data
     */
    private String generateQRCodeData(String password) {
        return "attendance:" + password;
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
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     ATTENDANCE REPORT                                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        System.out.println("\nCourse: " + course.getCourseCode() + " - " + course.getCourseName());
        System.out.println("Date: " + course.getAttendanceDateTime());
        System.out.println("Time Window: " + course.getAttendanceStartTime() + " - " + course.getAttendanceEndTime());
        
        List<String> enrolledStudentIds = course.getEnrolledStudentIds();
        
        if (enrolledStudentIds.isEmpty()) {
            System.out.println("\n⚠ No students enrolled in this course.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Create table header
        System.out.println("\n" + "═".repeat(88));
        System.out.printf("%-3s | %-20s | %-12s | %-15s | %-12s | %-15s\n", 
            "No.", "Student Name", "Student ID", "Status", "Marked By", "Time");
        System.out.println("═".repeat(88));
        
        int count = 1;
        int presentCount = 0;
        int lateCount = 0;
        int absentCount = 0;
        int excusedCount = 0;
        
        // Display each student's attendance
        for (String studentId : enrolledStudentIds) {
            Student student = students.get(studentId);
            if (student != null) {
                // Get attendance record for this student
                AttendanceRecord record = attendanceManager.findAttendanceRecord(
                    course.getCourseId(), studentId);
                
                String status = "ABSENT";
                String markedBy = "-";
                String time = "-";
                
                if (record != null) {
                    status = record.getStatus().toString();
                    markedBy = record.getMarkedBy();
                    time = record.getFormattedTimestamp().split(" ")[1]; // Extract time only
                    
                    // Count statuses
                    switch (record.getStatus()) {
                        case PRESENT:
                            presentCount++;
                            break;
                        case LATE:
                            lateCount++;
                            break;
                        case ABSENT:
                            absentCount++;
                            break;
                        case EXCUSED:
                            excusedCount++;
                            break;
                    }
                } else {
                    absentCount++;
                }
                
                // Format status with emoji
                String statusDisplay = formatStatusWithEmoji(status);
                
                System.out.printf("%-3d | %-20s | %-12s | %-15s | %-12s | %-15s\n",
                    count++,
                    student.getFullName().length() > 20 ? student.getFullName().substring(0, 17) + "..." : student.getFullName(),
                    studentId,
                    statusDisplay,
                    markedBy,
                    time);
            }
        }
        
        System.out.println("═".repeat(88));
        
        // Display summary statistics
        System.out.println("\n📊 ATTENDANCE SUMMARY:");
        System.out.println("  ✓ Present: " + presentCount);
        System.out.println("  ⏰ Late: " + lateCount);
        System.out.println("  ✗ Absent: " + absentCount);
        System.out.println("  ⓘ Excused: " + excusedCount);
        System.out.println("  Total: " + enrolledStudentIds.size());
        
        double attendanceRate = ((presentCount + lateCount + excusedCount) * 100.0) / enrolledStudentIds.size();
        System.out.printf("  Attendance Rate: %.1f%%\n", attendanceRate);
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Format status with emoji
     */
    private String formatStatusWithEmoji(String status) {
        switch (status) {
            case "PRESENT":
                return "✓ PRESENT";
            case "LATE":
                return "⏰ LATE";
            case "ABSENT":
                return "✗ ABSENT";
            case "EXCUSED":
                return "ⓘ EXCUSED";
            default:
                return status;
        }
    }
        
    /**
     * Check if current time is within attendance window
     */
    private boolean isWithinAttendanceWindow(Course course) {
        String startTime = course.getAttendanceStartTime();
        String endTime = course.getAttendanceEndTime();
        
        if (startTime == null || endTime == null) {
            System.out.println("⚠ No attendance time window set for this course.");
            return false;
        }

        // Get current time
        java.time.LocalTime currentTime = java.time.LocalTime.now();
        java.time.LocalTime start = java.time.LocalTime.parse(startTime);
        java.time.LocalTime end = java.time.LocalTime.parse(endTime);

        if (currentTime.isBefore(start)) {
            System.out.println("✗ Attendance window has not started yet. Available at: " + startTime);
            return false;
        }

        if (currentTime.isAfter(end)) {
            System.out.println("✗ Attendance window has ended. Was available until: " + endTime);
            return false;
        }

        System.out.println("✓ Attendance window is open. Valid until: " + endTime);
        return true;
    }

    /**
     * Automatically mark absent students if attendance time has ended
     */
    private void autoMarkAbsentIfTimeEnded(Course course) {
        String endTime = course.getAttendanceEndTime();
        String startTime = course.getAttendanceStartTime();
        
        // No attendance window set, skip
        if (endTime == null || startTime == null) {
            return;
        }

        // Check if current time is after end time
        java.time.LocalTime currentTime = java.time.LocalTime.now();
        java.time.LocalTime end = java.time.LocalTime.parse(endTime);

        // If time hasn't ended yet, don't mark absent
        if (currentTime.isBefore(end) || currentTime.equals(end)) {
            return;
        }

        List<String> enrolledStudents = course.getEnrolledStudentIds();
        List<Lesson> lessons = course.getLessons();
        
        if (lessons.isEmpty()) {
            return;
        }

        // Use the course ID for marking attendance
        String courseId = course.getCourseId();
        
        int markedAbsent = 0;
        
        for (String studentId : enrolledStudents) {
            // Check if student has already marked attendance
            AttendanceRecord existingRecord = attendanceManager.findAttendanceRecord(
                    courseId, studentId);
            
            if (existingRecord == null) {
                // Student hasn't marked attendance, mark as ABSENT
                attendanceManager.markAttendance(
                        courseId,
                        studentId,
                        AttendanceRecord.AttendanceStatus.ABSENT,
                        currentUser.getUserId(),
                        course.getPassword()
                );
                markedAbsent++;
            }
        }

        // Only show message if students were marked absent
        if (markedAbsent > 0) {
            System.out.println("\n⏰ Attendance window ended at " + endTime);
            System.out.println("✓ Automatically marked " + markedAbsent + " student(s) as ABSENT");
        }
    }

    /**
     * Validate date format (DD/MM/YYYY) and ensure it's not in the future
     */
    private boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        // Check format: DD/MM/YYYY
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false;
        }

        String[] parts = date.split("/");
        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Validate day
            if (day < 1 || day > 31) {
                return false;
            }

            // Validate month
            if (month < 1 || month > 12) {
                return false;
            }

            // Validate year (reasonable range)
            if (year < 2000 || year > 2100) {
                return false;
            }

            // Check days in month
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // Check for leap year
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                daysInMonth[1] = 29;
            }

            if (day > daysInMonth[month - 1]) {
                return false;
            }

            // Check if date is in the future
            LocalDateTime inputDate = LocalDateTime.of(year, month, day, 0, 0, 0);
            LocalDateTime today = LocalDateTime.now();
            if (inputDate.toLocalDate().isAfter(today.toLocalDate())) {
                System.out.println("✗ Invalid date! Attendance date cannot be in the future.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate time format (HH:MM)
     */
    private boolean isValidTime(String time) {
        if (time == null || time.isEmpty()) {
            return false;
        }

        // Check format: HH:MM
        if (!time.matches("\\d{2}:\\d{2}")) {
            return false;
        }

        String[] parts = time.split(":");
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            // Validate hour (0-23)
            if (hour < 0 || hour > 23) {
                return false;
            }

            // Validate minute (0-59)
            if (minute < 0 || minute > 59) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        AttendanceDashboard dashboard = new AttendanceDashboard();
        dashboard.start();
    }
}