package com.deepak.proexpenditure.pro_expenditure.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    // Generate a 16-byte salt
    public static String generateSalt() {
        byte[] salt = new byte[16]; // 128-bit salt
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
