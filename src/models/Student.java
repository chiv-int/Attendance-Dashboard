package models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private final String studentId;
    private final String program;
    private final int year;
    private final List<String> enrolledCourseIds;
    
    // Regular constructor - hashes password
    public Student(String userId, String username, String password,
                   String fullName, String studentId, String program, int year) {
        super(userId, username, password, fullName, "", "");
        this.studentId = studentId;
        this.program = program;
        this.year = year;
        this.enrolledCourseIds = new ArrayList<>();
    }
    
    // Constructor for database loading - password already hashed
    public Student(String userId, String username, String passwordHash,
                   String fullName, String studentId, String program, int year, boolean isPasswordHashed) {
        super(userId, username, passwordHash, fullName, "", "", isPasswordHashed);
        this.studentId = studentId;
        this.program = program;
        this.year = year;
        this.enrolledCourseIds = new ArrayList<>();
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getProgram() {
        return program;
    }
    
    public int getYear() {
        return year;
    }
    
    public List<String> getEnrolledCourseIds() {
        return new ArrayList<>(enrolledCourseIds);
    }
    
    public void enrollCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }
    
    public void dropCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return String.format("Student: %s - %s (Year %d, %s)",
                studentId, fullName, year, program);
    }
}
