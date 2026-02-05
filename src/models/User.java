package models;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class User {
    protected String id;
    protected String username;
    protected String passwordHash;
    protected String fullName;
    protected String email;
    protected String phone;
    
    // Regular constructor - hashes the password
    public User(String id, String username, String password, String fullName, String email, String phone) {
        this(id, username, password, fullName, email, phone, false);
    }
    
    // Constructor with flag for already-hashed passwords (from database)
    protected User(String id, String username, String password, String fullName, String email, String phone, boolean isPasswordHashed) {
        this.id = id;
        this.username = username;
        this.passwordHash = isPasswordHashed ? password : hashPassword(password);
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            return password; // Fallback to plain text (not recommended for production)
        }
    }
    
    public boolean verifyPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }
    
    public String getId() {
        return id;
    }
    
    public String getUserId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public abstract String getRole();
    
    public boolean isStudent() {
        return this instanceof Student;
    }
    
    public boolean isTeacher() {
        return this instanceof Teacher;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", id, fullName, getRole());
    }
}
