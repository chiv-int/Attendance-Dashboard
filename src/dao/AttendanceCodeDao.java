package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnection;

/**
 * Data Access Object for Attendance Codes
 * Manages attendance code generation and retrieval from database
 */
public class AttendanceCodeDao {
    private Connection connection;

    public AttendanceCodeDao() {
        try {
            DatabaseConnection dbConn = DatabaseConnection.getInstance();
            if (dbConn != null) {
                this.connection = dbConn.getConnection();
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection in AttendanceCodeDao: " + e.getMessage());
            this.connection = null;
        }
    }
    
    // ... rest of the code remains the samepackage dao;

    /**
     * Inner class to represent an Attendance Code
     */
    public static class AttendanceCodeRecord {
        private int codeId;
        private String courseId;
        private String password;
        private String qrCodeData;
        private LocalDate attendanceDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean isActive;
        private String createdBy;
        private Timestamp createdAt;

        // Constructors
        public AttendanceCodeRecord() {}

        public AttendanceCodeRecord(String courseId, String password, String qrCodeData,
                                   LocalDate attendanceDate, LocalTime startTime, 
                                   LocalTime endTime, String createdBy) {
            this.courseId = courseId;
            this.password = password;
            this.qrCodeData = qrCodeData;
            this.attendanceDate = attendanceDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.createdBy = createdBy;
            this.isActive = true;
        }

        // Getters and Setters
        public int getCodeId() { return codeId; }
        public void setCodeId(int codeId) { this.codeId = codeId; }

        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getQrCodeData() { return qrCodeData; }
        public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }

        public LocalDate getAttendanceDate() { return attendanceDate; }
        public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }

        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

        public Timestamp getCreatedAt() { return createdAt; }
        public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    }

    /**
     * Save a new attendance code to the database
     */
    public boolean saveAttendanceCode(AttendanceCodeRecord code) {
        if (connection == null) {
            System.err.println("✗ Database connection is null!");
            return false;
        }
        
        String sql = "INSERT INTO attendance_codes (course_id, password, qr_code_data, " +
                    "attendance_date, start_time, end_time, is_active, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, code.getCourseId());
            stmt.setString(2, code.getPassword());
            stmt.setString(3, code.getQrCodeData());
            stmt.setDate(4, Date.valueOf(code.getAttendanceDate()));
            stmt.setTime(5, Time.valueOf(code.getStartTime()));
            stmt.setTime(6, Time.valueOf(code.getEndTime()));
            stmt.setBoolean(7, code.isActive());
            stmt.setString(8, code.getCreatedBy());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated code ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        code.setCodeId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error saving attendance code!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get active attendance code for a course
     */
    public AttendanceCodeRecord getActiveCodeForCourse(String courseId) {
        String sql = "SELECT * FROM attendance_codes WHERE course_id = ? AND is_active = TRUE " +
                    "ORDER BY created_at DESC LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAttendanceCodeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving active attendance code!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Validate attendance code (check password only, time window checked separately)
     */
    public boolean validateAttendanceCode(String courseId, String password) {
        if (connection == null) {
            System.err.println("[DEBUG] Connection is NULL!");
            return false;
        }
        
        // Trim the input password to remove any whitespace
        password = password.trim();
        
        String sql = "SELECT * FROM attendance_codes WHERE course_id = ? AND TRIM(password) = ? AND is_active = 1 LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            stmt.setString(2, password);
            
            System.out.println("[DEBUG] Validating - CourseID: '" + courseId + "', Password: '" + password + "'");
            System.out.println("[DEBUG] Password length: " + password.length());
            
            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = rs.next();
                System.out.println("[DEBUG] Validation result: " + (found ? "FOUND" : "NOT FOUND"));
                return found;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error validating attendance code: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if attendance window is currently active
     */
    public boolean isAttendanceWindowActive(String courseId) {
        AttendanceCodeRecord code = getActiveCodeForCourse(courseId);
        
        if (code == null) {
            return false;
        }
        
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        
        return code.getAttendanceDate().equals(today) &&
               !now.isBefore(code.getStartTime()) &&
               !now.isAfter(code.getEndTime());
    }

    /**
     * Deactivate an attendance code
     */
    public boolean deactivateCode(int codeId) {
        String sql = "UPDATE attendance_codes SET is_active = FALSE WHERE code_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error deactivating attendance code!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all attendance codes for a course
     */
    public List<AttendanceCodeRecord> getCodesForCourse(String courseId) {
        List<AttendanceCodeRecord> codes = new ArrayList<>();
        String sql = "SELECT * FROM attendance_codes WHERE course_id = ? ORDER BY created_at DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    codes.add(extractAttendanceCodeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving attendance codes!");
            e.printStackTrace();
        }
        return codes;
    }

    /**
     * Get attendance code by ID
     */
    public AttendanceCodeRecord getCodeById(int codeId) {
        String sql = "SELECT * FROM attendance_codes WHERE code_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAttendanceCodeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving attendance code!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract AttendanceCodeRecord from ResultSet
     */
    private AttendanceCodeRecord extractAttendanceCodeFromResultSet(ResultSet rs) throws SQLException {
        AttendanceCodeRecord code = new AttendanceCodeRecord();
        code.setCodeId(rs.getInt("code_id"));
        code.setCourseId(rs.getString("course_id"));
        code.setPassword(rs.getString("password"));
        code.setQrCodeData(rs.getString("qr_code_data"));
        code.setAttendanceDate(rs.getDate("attendance_date").toLocalDate());
        code.setStartTime(rs.getTime("start_time").toLocalTime());
        code.setEndTime(rs.getTime("end_time").toLocalTime());
        code.setActive(rs.getBoolean("is_active"));
        code.setCreatedBy(rs.getString("created_by"));
        code.setCreatedAt(rs.getTimestamp("created_at"));
        return code;
    }

    /**
     * Delete old inactive codes (cleanup)
     */
    public int deleteInactiveCodes(int daysOld) {
        String sql = "DELETE FROM attendance_codes WHERE is_active = FALSE " +
                    "AND created_at < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, daysOld);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("✗ Error deleting old attendance codes!");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the code_id for a specific course and password
     */
    public Integer getCodeIdByCourseAndPassword(String courseId, String password) {
        String sql = "SELECT code_id FROM attendance_codes WHERE course_id = ? AND password = ? AND is_active = 1 LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("code_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting code_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
