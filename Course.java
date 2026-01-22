import java.util.*;

public class Course {
    private String courseName;
    private String password;
    private final List<AttendanceRecord> attendanceRecords;
    private final List<Lesson> lessons;
    private final List<Exercise> exercises;
    
    public Course(String courseName, String password) {
        this.courseName = courseName;
        this.password = password;
        this.attendanceRecords = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.exercises = new ArrayList<>();
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
    
    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }
    
    public void addAttendanceRecord(AttendanceRecord record) {
        attendanceRecords.add(record);
    }
    
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }
    
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }
    
    public List<Lesson> getLessons() {
        return lessons;
    }
    
    public List<Exercise> getExercises() {
        return exercises;
    }
}

class Lesson {
    private final String title;
    private final String content;
    
    public Lesson(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
}

class Exercise {
    private final String title;
    private final String description;
    
    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
}

class AttendanceRecord {
    private final int recordNumber;
    private final String studentName;
    private final String major;
    private final AttendanceStatus status;
    private final java.time.LocalDateTime timestamp;
    
    public AttendanceRecord(int recordNumber, String studentName, String major, AttendanceStatus status) {
        this.recordNumber = recordNumber;
        this.studentName = studentName;
        this.major = major;
        this.status = status;
        this.timestamp = java.time.LocalDateTime.now();
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
    
    public java.time.LocalDateTime getTimestamp() {
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

enum AttendanceStatus {
    PRESENT, LATE
}
