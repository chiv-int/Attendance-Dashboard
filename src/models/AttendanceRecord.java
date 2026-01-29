package models;

import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AttendanceRecord class to track attendance for a specific lesson
 */
public class AttendanceRecord {
    private final int recordNumber;
    private final String studentName;
    private final String major;
    private final AttendanceStatus status;
    private final LocalDateTime timestamp;
    
    public AttendanceRecord(int recordNumber, String studentName, String major, AttendanceStatus status) {
        this.recordNumber = recordNumber;
        this.studentName = studentName;
        this.major = major;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
    
    public int getRecordNumber() {
        return recordNumber;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public String getMajor() {
        return major;
    }
    
    public AttendanceStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        String dateStr = String.format("%02d/%02d/%04d", 
            timestamp.getDayOfMonth(), 
            timestamp.getMonthValue(), 
            timestamp.getYear());
        return String.format("%-3d %-20s %-15s %-20s %-10s", 
            recordNumber, studentName, major, dateStr, status);
    }
}
