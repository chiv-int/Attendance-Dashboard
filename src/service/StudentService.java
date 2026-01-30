package service;

import models.*;
import java.util.*;

/**
 * StudentService - Responsibility: Orchestrate student-related operations
 */
public class StudentService {
    private final CourseRepository courseRepository;
    private final AttendanceService attendanceService;
    private final CourseContentService contentService;
    
    public StudentService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.attendanceService = new AttendanceService(courseRepository);
        this.contentService = new CourseContentService(courseRepository);
    }
    
    /**
     * Mark attendance for a course
     */
    public boolean markAttendance(String courseName, String password, 
                                  String studentName, String major, 
                                  AttendanceRecord.AttendanceStatus status) {
        return attendanceService.markAttendance(courseName, password, studentName, major, status);
    }
    
    /**
     * View all available courses
     */
    public void viewAvailableCourses() {
        Map<String, Course> courses = courseRepository.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        
        System.out.println("\n=== Available Courses ===");
        for (String courseName : courses.keySet()) {
            System.out.println("- " + courseName);
        }
    }
    
    /**
     * View lessons for a course
     */
    public void viewLessons(String courseName) {
        contentService.viewLessons(courseName);
    }
    
    /**
     * View exercises for a course
     */
    public void viewExercises(String courseName) {
        contentService.viewExercises(courseName);
    }
}
