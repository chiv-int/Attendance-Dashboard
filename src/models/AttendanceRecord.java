package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AttendanceRecord model - Represents a single attendance entry
 */
public class AttendanceRecord {
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
    
    private final String recordId;
    private final String lessonId;
    private final String studentId;
    private AttendanceStatus status;
    private final String markedBy; // Teacher ID who marked attendance
    private final LocalDateTime timestamp;
    
    // Full constructor
    public AttendanceRecord(String recordId, String lessonId, String studentId,
                          AttendanceStatus status, String markedBy) {
        this.recordId = recordId;
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.status = status;
        this.markedBy = markedBy;
        this.timestamp = LocalDateTime.now();
    }
    
    // Simplified constructor for backward compatibility
    public AttendanceRecord(int recordNumber, String studentName, String major, 
                          AttendanceRecord.AttendanceStatus status) {
        this("REC-" + recordNumber, null, studentName, status, null);
    }
    
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
    
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
    
    public String getMarkedBy() {
        return markedBy;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("Attendance[%s]: Student=%s, Lesson=%s, Status=%s, Time=%s",
                recordId, studentId, lessonId, status, getFormattedTimestamp());
    }
}
