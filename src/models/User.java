package models;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * User entity class for authentication and role management
 * Supports both Student and Teacher roles with SHA-256 password hashing
 */
public class User {
    private String userId;
    private String username;
    private String passwordHash;
    private UserRole role;
    private String fullName;

    public enum UserRole {
        STUDENT,
        TEACHER
    }

    public User(String userId, String username, String password, UserRole role, String fullName) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.fullName = fullName;
    }

    /**
     * Hash password using SHA-256 algorithm
     * @param password Plain text password
     * @return Hashed password as hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Convert byte array to hexadecimal string
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Verify if the provided password matches the stored hash
     */
    public boolean verifyPassword(String password) {
        String hashedInput = hashPassword(password);
        return this.passwordHash.equals(hashedInput);
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isStudent() {
        return role == UserRole.STUDENT;
    }

    public boolean isTeacher() {
        return role == UserRole.TEACHER;
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', role=%s, name='%s'}",
                userId, username, role, fullName);
    }
}