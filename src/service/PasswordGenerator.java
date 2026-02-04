package service;

import java.util.Random;


public class PasswordGenerator {

    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}
