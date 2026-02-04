package service;

import models.*;
import java.util.*;


public class StudentService {
    private final CourseRepository courseRepository;
    private final AttendanceService attendanceService;
    private final CourseContentService contentService;
    
    public StudentService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.attendanceService = new AttendanceService(courseRepository);
        this.contentService = new CourseContentService(courseRepository);
    }
    

    public boolean markAttendance(String courseName, String password, 
                                  String studentName, String major, 
                                  AttendanceRecord.AttendanceStatus status) {
        return attendanceService.markAttendance(courseName, password, studentName, major, status);
    }

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
    public void viewLessons(String courseName) {
        contentService.viewLessons(courseName);
    }
    

    public void viewExercises(String courseName) {
        contentService.viewExercises(courseName);
    }
}
