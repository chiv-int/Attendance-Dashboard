package models;

import java.util.*;

/**
 * Course model - Responsibility: Store course information
 */
public class Course {
    private String courseName;
    private String password;
    private final List<AttendanceRecord> attendanceRecords;
    private final List<Lesson> lessons;
    private final List<Exercise> exercises;
    
    public Course(String courseName, String password) {
        this.courseName = courseName;
        this.password = password;
        this.attendanceRecords = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.exercises = new ArrayList<>();
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
    
    public List<Lesson> getLessons() {
        return lessons;
    }
    
    public List<Exercise> getExercises() {
        return exercises;
    }
}
