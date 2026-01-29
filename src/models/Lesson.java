package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lesson class representing a single lesson/study session
 */
public class Lesson {
    private final String lessonId;
    private final String courseId;
    private final String title;
    private final String description;
    private final LocalDateTime dateTime;
    private final int duration; // in minutes
    private final String location;

    public Lesson(String lessonId, String courseId, String title, String description,
                  LocalDateTime dateTime, int duration, String location) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.duration = duration;
        this.location = location;
    }

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    // Getters
    public String getLessonId() {
        return lessonId;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Lesson: %s | %s | %s | %d mins",
                title, getFormattedDateTime(), location, duration);
    }
}
