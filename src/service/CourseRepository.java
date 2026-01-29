package service;

import models.Course;
import java.util.*;

/**
 * CourseRepository - Responsibility: Manage course storage and retrieval
 */
public class CourseRepository {
    private final Map<String, Course> courses;
    
    public CourseRepository() {
        this.courses = new HashMap<>();
    }
    
    /**
     * Create and store a new course
     */
    public Course createCourse(String courseName) {
        Course course = new Course(courseName, null);
        courses.put(courseName, course);
        System.out.println("Course created: " + courseName);
        return course;
    }
    
    /**
     * Get a course by name
     */
    public Course getCourse(String courseName) {
        return courses.get(courseName);
    }
    
    /**
     * Get all courses
     */
    public Map<String, Course> getAllCourses() {
        return new HashMap<>(courses);
    }
    
    /**
     * Check if course exists
     */
    public boolean courseExists(String courseName) {
        return courses.containsKey(courseName);
    }
    
    /**
     * Delete a course
     */
    public void deleteCourse(String courseName) {
        courses.remove(courseName);
    }
}
