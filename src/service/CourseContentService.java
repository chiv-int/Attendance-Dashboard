package service;

import models.*;
import java.util.*;


public class CourseContentService {
    private final CourseRepository courseRepository;
    
    public CourseContentService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void addLesson(String courseName, String title, String content) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        Lesson lesson = new Lesson(title, content);
        course.addLesson(lesson);
        System.out.println("Lesson added: " + title);
    }
    

    public void addExercise(String courseName, String title, String description) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        Exercise exercise = new Exercise(title, description);
        course.addExercise(exercise);
        System.out.println("Exercise added: " + title);
    }

    public void viewLessons(String courseName) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<Lesson> lessons = course.getLessons();
        if (lessons.isEmpty()) {
            System.out.println("No lessons available.");
            return;
        }
        
        System.out.println("\n=== Lessons for " + courseName + " ===");
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            System.out.println((i + 1) + ". " + lesson.getTitle());
            System.out.println("   " + lesson.getContent());
        }
    }

    public void viewExercises(String courseName) {
        Course course = courseRepository.getCourse(courseName);
        if (course == null) {
            System.out.println("Error: Course not found!");
            return;
        }
        
        List<Exercise> exercises = course.getExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises available.");
            return;
        }
        
        System.out.println("\n=== Exercises for " + courseName + " ===");
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            System.out.println((i + 1) + ". " + exercise.getTitle());
            System.out.println("   " + exercise.getDescription());
        }
    }
}
