package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.AttendanceRecord;
import util.DatabaseConnection;

/**
 * Data Access Object for Attendance Records
 * Handles attendance record operations in the database
 */
public class AttendanceDao {
    private final Connection connection;

    public AttendanceDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Save attendance record
     */
    public boolean saveAttendanceRecord(AttendanceRecord record, Integer codeId) {
        String sql = "INSERT INTO attendance_records (course_id, student_id, status, marked_by, notes, code_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE status = ?, marked_by = ?, notes = ?, code_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String courseId = record.getCourseId();
            String studentId = record.getStudentId();
            
            // Debug logging
            System.out.println("[DEBUG] DAO: Saving attendance - CourseId: " + courseId + ", StudentId: " + studentId);
            System.out.println("[DEBUG] DAO: Connection is: " + (connection != null ? "NOT NULL" : "NULL"));
            System.out.println("[DEBUG] DAO: Status: " + record.getStatus() + ", MarkedBy: " + record.getMarkedBy());
            
            stmt.setString(1, courseId);
            stmt.setString(2, studentId);
            stmt.setString(3, record.getStatus().toString());
            stmt.setString(4, record.getMarkedBy());
            stmt.setString(5, ""); // notes not in model
            if (codeId != null) {
                stmt.setInt(6, codeId);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            // For ON DUPLICATE KEY UPDATE
            stmt.setString(7, record.getStatus().toString());
            stmt.setString(8, record.getMarkedBy());
            stmt.setString(9, "");
            if (codeId != null) {
                stmt.setInt(10, codeId);
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            int result = stmt.executeUpdate();
            System.out.println("[DEBUG] DAO: SQL executeUpdate returned: " + result);
            
            // Explicit commit to ensure data is saved
            if (!connection.getAutoCommit()) {
                connection.commit();
                System.out.println("[DEBUG] DAO: Transaction committed");
            }
            
            if (result > 0) {
                System.out.println("✓ Attendance record saved to database successfully!");
                System.out.println("[DEBUG] Database: " + connection.getCatalog());
                
                // Verify the record was actually saved
                String verifySQL = "SELECT COUNT(*) FROM attendance_records WHERE course_id = ? AND student_id = ?";
                try (PreparedStatement verifyStmt = connection.prepareStatement(verifySQL)) {
                    verifyStmt.setString(1, courseId);
                    verifyStmt.setString(2, studentId);
                    try (ResultSet rs = verifyStmt.executeQuery()) {
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            System.out.println("[DEBUG] Verification: Found " + count + " record(s) in database");
                        }
                    }
                } catch (SQLException verifyEx) {
                    System.err.println("[DEBUG] Verification query failed: " + verifyEx.getMessage());
                }
                
                return true;
            } else {
                System.out.println("✗ DAO: executeUpdate returned 0 - record may not have been saved");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error saving attendance record!");
            System.err.println("✗ Error: " + e.getMessage());
            System.err.println("✗ SQL State: " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get attendance record for a student in a course
     */
    public AttendanceRecord getAttendanceRecord(String courseId, String studentId) {
        String sql = "SELECT * FROM attendance_records WHERE course_id = ? AND student_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            stmt.setString(2, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAttendanceRecordFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving attendance record!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all attendance records for a course
     */
    public List<AttendanceRecord> getAttendanceByCourse(String courseId) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance_records WHERE course_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving attendance records!");
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get all attendance records for a student
     */
    public List<AttendanceRecord> getAttendanceByStudent(String studentId) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance_records WHERE student_id = ? ORDER BY marked_at DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving student attendance records!");
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance statistics for a student in a course
     */
    public AttendanceStats getStudentAttendanceStats(String studentId, String courseId) {
        String sql = "SELECT " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) as present, " +
                    "SUM(CASE WHEN status = 'ABSENT' THEN 1 ELSE 0 END) as absent, " +
                    "SUM(CASE WHEN status = 'LATE' THEN 1 ELSE 0 END) as late, " +
                    "SUM(CASE WHEN status = 'EXCUSED' THEN 1 ELSE 0 END) as excused " +
                    "FROM attendance_records " +
                    "WHERE student_id = ? AND course_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AttendanceStats(
                        rs.getInt("total"),
                        rs.getInt("present"),
                        rs.getInt("absent"),
                        rs.getInt("late"),
                        rs.getInt("excused")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving attendance statistics!");
            e.printStackTrace();
        }
        return new AttendanceStats(0, 0, 0, 0, 0);
    }

    /**
     * Extract AttendanceRecord from ResultSet
     */
    private AttendanceRecord extractAttendanceRecordFromResultSet(ResultSet rs) throws SQLException {
        AttendanceRecord.AttendanceStatus status = 
            AttendanceRecord.AttendanceStatus.valueOf(rs.getString("status"));
        
        AttendanceRecord record = new AttendanceRecord(
            "REC-" + rs.getInt("record_id"),
            rs.getString("course_id"),
            rs.getString("student_id"),
            status,
            rs.getString("marked_by")
        );
        return record;
    }

    /**
     * Inner class for attendance statistics
     */
    public static class AttendanceStats {
        private final int total;
        private final int present;
        private final int absent;
        private final int late;
        private final int excused;

        public AttendanceStats(int total, int present, int absent, int late, int excused) {
            this.total = total;
            this.present = present;
            this.absent = absent;
            this.late = late;
            this.excused = excused;
        }

        public int getTotal() { return total; }
        public int getPresent() { return present; }
        public int getAbsent() { return absent; }
        public int getLate() { return late; }
        public int getExcused() { return excused; }
        
        public double getAttendanceRate() {
            if (total == 0) return 0.0;
            return (double) present / total * 100;
        }
    }
}
