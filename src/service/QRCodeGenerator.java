package service;


public class QRCodeGenerator {
    

    public String generateQRData(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        System.out.println("QR Code generated");
        System.out.println("QR Data: " + password);
        return password;
    }
}
