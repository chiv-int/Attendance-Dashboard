package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Quiz class representing a course quiz
 */
public class Quiz {
    private final String quizId;
    private final String courseId;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private final int totalPoints;
    private final int durationMinutes;

    public Quiz(String quizId, String courseId, String title, String description,
                LocalDateTime dueDate, int totalPoints, int durationMinutes) {
        this.quizId = quizId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.totalPoints = totalPoints;
        this.durationMinutes = durationMinutes;
    }

    public String getFormattedDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dueDate.format(formatter);
    }

    // Getters
    public String getQuizId() {
        return quizId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return String.format("Quiz: %s | Due: %s | Points: %d | Duration: %d mins",
                title, getFormattedDueDate(), totalPoints, durationMinutes);
    }
}