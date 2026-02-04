package models;

import dao.AttendanceDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AttendanceManager {
    private final Map<String, AttendanceRecord> attendanceRecords; // recordId -> AttendanceRecord
    private final Map<String, List<String>> courseAttendance; // courseId -> List of recordIds
    private final String attendancePassword; // Password required to mark attendance
    private final String attendancePasswordHash;
    private final AttendanceDao attendanceDao;

    public AttendanceManager(String attendancePassword) {
        this.attendanceRecords = new HashMap<>();
        this.courseAttendance = new HashMap<>();
        this.attendancePassword = attendancePassword;
        this.attendancePasswordHash = User.hashPassword(attendancePassword);
        
        // Initialize DAO
        try {
            this.attendanceDao = new AttendanceDao();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AttendanceDao: " + e.getMessage());
        }
    }
    public boolean verifyAttendancePassword(String password) {
        return User.hashPassword(password).equals(attendancePasswordHash);
    }

    public boolean markAttendance(String courseId, String studentId,
                                  AttendanceRecord.AttendanceStatus status,
                                  String markedBy, String password) {
        // Validate password against this specific course's attendance code
        if (!validateCourseAttendanceCode(courseId, password)) {
            System.out.println("✗ Invalid attendance code for this course!");
            System.out.println("✗ The password you entered does not match this course's attendance code.");
            return false;
        }

        // Check if attendance already exists
        AttendanceRecord existing = findAttendanceRecord(courseId, studentId);
        if (existing != null) {
            existing.setStatus(status);
            // Get the code_id for this course
            Integer codeId = getAttendanceCodeIdForCourse(courseId, password);
            // Save updated record to database
            if (attendanceDao != null) {
                attendanceDao.saveAttendanceRecord(existing, codeId);
            }
            System.out.println("✓ Attendance updated successfully!");
            return true;
        }

        // Create new attendance record
        String recordId = "ATT-" + System.currentTimeMillis();
        AttendanceRecord record = new AttendanceRecord(recordId, courseId, studentId, status, markedBy);

        attendanceRecords.put(recordId, record);
        courseAttendance.computeIfAbsent(courseId, k -> new ArrayList<>()).add(recordId);
        
        // Get the code_id for this course and save with it
        Integer codeId = getAttendanceCodeIdForCourse(courseId, password);
        
        // Save record to database with code_id
        if (attendanceDao != null) {
            attendanceDao.saveAttendanceRecord(record, codeId);
        }

        System.out.println("✓ Attendance marked successfully!");
        return true;
    }

    private boolean validateCourseAttendanceCode(String courseId, String password) {
        try {
            // Query attendance_codes table to find code for this course
            dao.AttendanceCodeDao codeDao = new dao.AttendanceCodeDao();
            return codeDao.validateAttendanceCode(courseId, password);
        } catch (Exception e) {
            System.err.println("Error validating attendance code: " + e.getMessage());
            return false;
        }
    }


    private Integer getAttendanceCodeIdForCourse(String courseId, String password) {
        try {
            dao.AttendanceCodeDao codeDao = new dao.AttendanceCodeDao();
            return codeDao.getCodeIdByCourseAndPassword(courseId, password);
        } catch (Exception e) {
            System.err.println("Error getting code_id: " + e.getMessage());
            return null;
        }
    }


    public AttendanceRecord findAttendanceRecord(String courseId, String studentId) {
        List<String> recordIds = courseAttendance.get(courseId);
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


    public List<AttendanceRecord> getCourseAttendance(String courseId) {
        List<String> recordIds = courseAttendance.getOrDefault(courseId, new ArrayList<>());
        return recordIds.stream()
                .map(attendanceRecords::get)
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getStudentAttendance(String studentId) {
        return attendanceRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

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


    public void displayCourseAttendanceSummary(String courseId, Course course) {
        List<AttendanceRecord> records = getCourseAttendance(courseId);

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║      ATTENDANCE SUMMARY FOR COURSE         ║");
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
