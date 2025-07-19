package com.ejemplo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidades de seguridad para el manejo seguro de contraseñas y datos sensibles.
 */
public class SecurityUtils {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * Genera un salt aleatorio para encriptación
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Encripta una contraseña usando SHA-256 con salt
     */
    public static String encryptPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }
    
    /**
     * Encripta una contraseña generando un salt automáticamente
     */
    public static String encryptPassword(String password) {
        String salt = generateSalt();
        return encryptPassword(password, salt);
    }
    
    /**
     * Verifica si una contraseña coincide con su hash
     */
    public static boolean verifyPassword(String password, String hashedPassword, String salt) {
        String newHash = encryptPassword(password, salt);
        return newHash.equals(hashedPassword);
    }
    
    /**
     * Enmascara una contraseña para logging seguro
     */
    public static String maskPassword(String password) {
        if (password == null || password.length() <= 2) {
            return "***";
        }
        return password.charAt(0) + "***" + password.charAt(password.length() - 1);
    }
    
    /**
     * Genera una contraseña segura aleatoria
     */
    public static String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        
        // Asegurar al menos una mayúscula, una minúscula, un número y un símbolo
        password.append(chars.charAt(RANDOM.nextInt(26))); // Mayúscula
        password.append(chars.charAt(26 + RANDOM.nextInt(26))); // Minúscula
        password.append(chars.charAt(52 + RANDOM.nextInt(10))); // Número
        password.append(chars.charAt(62 + RANDOM.nextInt(8))); // Símbolo
        
        // Completar con caracteres aleatorios
        for (int i = 4; i < 12; i++) {
            password.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        
        // Mezclar los caracteres
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
    
    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
    }
} 