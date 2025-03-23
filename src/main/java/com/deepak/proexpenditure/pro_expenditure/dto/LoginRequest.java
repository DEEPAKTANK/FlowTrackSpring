package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String identifier; // ✅ Can be email, phone, or userId
    private String password;
}