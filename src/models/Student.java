package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Student class representing a student user
 * Stores student-specific information and enrolled courses
 */
public class Student extends User {
    private final String studentId;
    private final String major;
    private final int year;
    private final List<String> enrolledCourseIds;

    public Student(String userId, String username, String password, String fullName,
                   String studentId, String major, int year) {
        super(userId, username, password, UserRole.STUDENT, fullName);
        this.studentId = studentId;
        this.major = major;
        this.year = year;
        this.enrolledCourseIds = new ArrayList<>();
    }

    public void enrollCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public void dropCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }

    public boolean isEnrolledIn(String courseId) {
        return enrolledCourseIds.contains(courseId);
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getMajor() {
        return major;
    }

    public int getYear() {
        return year;
    }

    public List<String> getEnrolledCourseIds() {
        return new ArrayList<>(enrolledCourseIds);
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', major='%s', year=%d, courses=%d}",
                studentId, getFullName(), major, year, enrolledCourseIds.size());
    }
}