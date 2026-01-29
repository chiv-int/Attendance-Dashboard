package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AttendanceManager class to manage all attendance records
 */
public class AttendanceManager {
    private final Map<String, AttendanceRecord> attendanceRecords; // recordId -> AttendanceRecord
    private final Map<String, List<String>> lessonAttendance; // lessonId -> List of recordIds
    private final String attendancePassword; // Password required to mark attendance
    private final String attendancePasswordHash;

    public AttendanceManager(String attendancePassword) {
        this.attendanceRecords = new HashMap<>();
        this.lessonAttendance = new HashMap<>();
        this.attendancePassword = attendancePassword;
        this.attendancePasswordHash = User.hashPassword(attendancePassword);
    }

    /**
     * Verify attendance password
     */
    public boolean verifyAttendancePassword(String password) {
        return User.hashPassword(password).equals(attendancePasswordHash);
    }

    /**
     * Mark attendance for a student in a specific lesson
     */
    public boolean markAttendance(String lessonId, String studentId,
                                  AttendanceRecord.AttendanceStatus status,
                                  String markedBy, String password) {
        // Verify password
        if (!verifyAttendancePassword(password)) {
            System.out.println(" Invalid attendance password!");
            return false;
        }

        // Check if attendance already exists
        AttendanceRecord existing = findAttendanceRecord(lessonId, studentId);
        if (existing != null) {
            existing.setStatus(status);
            System.out.println(" Attendance updated successfully!");
            return true;
        }

        // Create new attendance record
        String recordId = "ATT-" + System.currentTimeMillis();
        AttendanceRecord record = new AttendanceRecord(recordId, lessonId, studentId, status, markedBy);

        attendanceRecords.put(recordId, record);
        lessonAttendance.computeIfAbsent(lessonId, k -> new ArrayList<>()).add(recordId);

        System.out.println("✓ Attendance marked successfully!");
        return true;
    }

    /**
     * Find attendance record for a specific student in a lesson
     */
    public AttendanceRecord findAttendanceRecord(String lessonId, String studentId) {
        List<String> recordIds = lessonAttendance.get(lessonId);
        if (recordIds == null) {
            return null;
        }

        for (String recordId : recordIds) {
            AttendanceRecord record = attendanceRecords.get(recordId);
            if (record != null && record.getStudentId().equals(studentId)) {
                return record;
            }
        }
        return null;
    }

    /**
     * Get all attendance records for a lesson
     */
    public List<AttendanceRecord> getLessonAttendance(String lessonId) {
        List<String> recordIds = lessonAttendance.getOrDefault(lessonId, new ArrayList<>());
        return recordIds.stream()
                .map(attendanceRecords::get)
                .collect(Collectors.toList());
    }

    /**
     * Get all attendance records for a student across all lessons
     */
    public List<AttendanceRecord> getStudentAttendance(String studentId) {
        return attendanceRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Calculate attendance percentage for a student in a course
     */
    public double calculateAttendancePercentage(String studentId, List<Lesson> courseLessons) {
        if (courseLessons.isEmpty()) {
            return 0.0;
        }

        long presentCount = courseLessons.stream()
                .filter(lesson -> {
                    AttendanceRecord record = findAttendanceRecord(lesson.getLessonId(), studentId);
                    return record != null && record.getStatus() == AttendanceRecord.AttendanceStatus.PRESENT;
                })
                .count();

        return (presentCount * 100.0) / courseLessons.size();
    }

    /**
     * Display attendance summary for a lesson
     */
    public void displayLessonAttendanceSummary(String lessonId, Course course) {
        List<AttendanceRecord> records = getLessonAttendance(lessonId);

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║      ATTENDANCE SUMMARY FOR LESSON         ║");
        System.out.println("╚════════════════════════════════════════════╝");

        long presentCount = records.stream()
                .filter(r -> r.getStatus() == AttendanceRecord.AttendanceStatus.PRESENT)
                .count();
        long absentCount = records.stream()
                .filter(r -> r.getStatus() == AttendanceRecord.AttendanceStatus.ABSENT)
                .count();

        System.out.println("Total Enrolled: " + course.getEnrolledStudentCount());
        System.out.println("Present: " + presentCount);
        System.out.println("Absent: " + absentCount);
        System.out.println("Not Marked: " + (course.getEnrolledStudentCount() - records.size()));

        if (!records.isEmpty()) {
            System.out.println("\nAttendance Records:");
            for (AttendanceRecord record : records) {
                System.out.println("  " + record);
            }
        }
    }

    public String getAttendancePassword() {
        return attendancePassword;
    }
}
