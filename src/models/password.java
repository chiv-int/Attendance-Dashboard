package models;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for hashing and verifying passwords using SHA-256 with salt
 */
public class password{
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hash a password with a random salt
     * @param password The plain text password
     * @return The hashed password in format: salt:hash
     */
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash the password with the salt
            String hash = hashWithSalt(password, salt);

            // Return salt:hash format
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            return saltBase64 + ":" + hash;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /**
     * Verify a password against a stored hash
     * @param password The plain text password to verify
     * @param storedHash The stored hash in format: salt:hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split the stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            String saltBase64 = parts[0];
            String expectedHash = parts[1];

            // Decode the salt
            byte[] salt = Base64.getDecoder().decode(saltBase64);

            // Hash the input password with the same salt
            String actualHash = hashWithSalt(password, salt);

            // Compare the hashes
            return actualHash.equals(expectedHash);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hash a password with a given salt
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}