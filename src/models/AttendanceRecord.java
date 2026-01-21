package models;

import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AttendanceRecord class to track attendance for a specific lesson
 */
public class AttendanceRecord {
    private final String recordId;
    private final String lessonId;
    private final String studentId;
    private AttendanceStatus status;
    private LocalDateTime markedAt;
    private final String markedBy; // User ID of who marked the attendance

    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        LATE,
        EXCUSED
    }

    public AttendanceRecord(String recordId, String lessonId, String studentId,
                            AttendanceStatus status, String markedBy) {
        this.recordId = recordId;
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.status = status;
        this.markedAt = LocalDateTime.now();
        this.markedBy = markedBy;
    }

    public String getFormattedMarkedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return markedAt.format(formatter);
    }

    // Getters
    public String getRecordId() {
        return recordId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getStudentId() {
        return studentId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    // Setter for status (in case it needs to be updated)
    public void setStatus(AttendanceStatus status) {
        this.status = status;
        this.markedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Attendance: Student %s | Status: %s | Marked: %s",
                studentId, status, getFormattedMarkedAt());
    }
}