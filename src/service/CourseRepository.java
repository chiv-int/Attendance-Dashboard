package service;

import models.Course;
import java.util.*;

public class CourseRepository {
    private final Map<String, Course> courses;
    
    public CourseRepository() {
        this.courses = new HashMap<>();
    }

    public Course createCourse(String courseName) {
        Course course = new Course(courseName, null);
        courses.put(courseName, course);
        System.out.println("Course created: " + courseName);
        return course;
    }

    public Course getCourse(String courseName) {
        return courses.get(courseName);
    }

    public Map<String, Course> getAllCourses() {
        return new HashMap<>(courses);
    }

    public boolean courseExists(String courseName) {
        return courses.containsKey(courseName);
    }
    

    public void deleteCourse(String courseName) {
        courses.remove(courseName);
    }
}
