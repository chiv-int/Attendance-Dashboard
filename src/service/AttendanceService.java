package service;

import java.util.*;
import models.*;

/**
 * AttendanceService - Responsibility: Handle all attendance-related operations
 */
public class AttendanceService {
    private final CourseRepository courseRepository;
    
    public AttendanceService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    /**
     * Mark student attendance for a course
     */
    public boolean markAttendance(String courseName, String password, 
                                  String studentName, String major, 
                                  AttendanceStatus status) {
        Course course = courseRepository.getCourse(courseName);
        
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
        
        // Create and add attendance record
        AttendanceRecord record = new AttendanceRecord(recordNumber, studentName, major, status);
        course.addAttendanceRecord(record);
        
        System.out.println("Attendance marked successfully!");
        System.out.println("Student: " + studentName + " - Status: " + status);
        return true;
    }
    
    /**
     * View all attendance records for a course
     */
    public void viewAttendanceRecords(String courseName) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<AttendanceRecord> records = course.getAttendanceRecords();
        if (records.isEmpty()) {
            System.out.println("No attendance records yet.");
            return;
        }
        
        System.out.println("\n=== Attendance Records for " + courseName + " ===");
        System.out.printf("%-3s %-20s %-15s %-20s %-10s\n", "No", "Name", "Major", "Date", "Status");
        System.out.println("=".repeat(75));
        
        for (AttendanceRecord record : records) {
            System.out.println(record);
        }
    }
}
