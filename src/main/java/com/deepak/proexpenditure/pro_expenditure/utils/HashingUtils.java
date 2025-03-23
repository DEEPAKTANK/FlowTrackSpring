package com.deepak.proexpenditure.pro_expenditure.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtils {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password, String salt) {
        return passwordEncoder.encode(password + salt); // Append salt before hashing
    }
}
