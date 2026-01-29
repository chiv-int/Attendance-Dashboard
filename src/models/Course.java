package models;

import java.util.*;

/**
 * Course model - Responsibility: Store course information
 */
public class Course {
    private final String courseId;
    private final String courseCode;
    private String courseName;
    private final String teacherId;
    private final String schedule;
    private final int credits;
    private String password;
    private final List<String> enrolledStudentIds;
    private final List<AttendanceRecord> attendanceRecords;
    private final List<Lesson> lessons;
    private final List<Exercise> exercises;
    private final List<Quiz> quizzes;
    
    // Full constructor for detailed course creation
    public Course(String courseId, String courseCode, String courseName,
                  String teacherId, String schedule, int credits) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherId = teacherId;
        this.schedule = schedule;
        this.credits = credits;
        this.password = null;
        this.enrolledStudentIds = new ArrayList<>();
        this.attendanceRecords = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.exercises = new ArrayList<>();
        this.quizzes = new ArrayList<>();
    }
    
    // Simplified constructor for backward compatibility
    public Course(String courseName, String password) {
        this(null, null, courseName, null, null, 0);
        this.password = password;
    }
    
    public void enrollStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
    
    public List<String> getEnrolledStudentIds() {
        return new ArrayList<>(enrolledStudentIds);
    }
    
    public int getEnrolledStudentCount() {
        return enrolledStudentIds.size();
    }
    
    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }
    
    public void addAttendanceRecord(AttendanceRecord record) {
        attendanceRecords.add(record);
    }
    
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }
    
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }
    
    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }
    
    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }
    
    public List<Exercise> getExercises() {
        return exercises;
    }
    
    public List<Quiz> getQuizzes() {
        return new ArrayList<>(quizzes);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) | Students: %d | Lessons: %d | Exercises: %d | Quizzes: %d",
                courseCode != null ? courseCode : "N/A", 
                courseName, 
                schedule != null ? schedule : "N/A",
                enrolledStudentIds.size(), lessons.size(), exercises.size(), quizzes.size());
    }
}
