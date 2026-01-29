package service;

/**
 * QRCodeGenerator - Responsibility: Generate QR codes from attendance password
 */
public class QRCodeGenerator {
    
    /**
     * Generate QR code data from password
     * For now, it returns the password as QR data
     * Can be extended to generate actual QR code in future
     */
    public String generateQRData(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        System.out.println("QR Code generated");
        System.out.println("QR Data: " + password);
        return password;
    }
}
