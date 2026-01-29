package models;

/**
 * Standalone AttendanceStatus enum for backward compatibility
 * Note: AttendanceRecord also has a nested AttendanceStatus enum
 */
public enum AttendanceStatus {
    PRESENT, ABSENT, LATE, EXCUSED
}
