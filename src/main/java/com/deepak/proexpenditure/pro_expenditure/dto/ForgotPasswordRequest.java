package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String userName;
    private String email; // optional
    private String phone; // optional

    // Getters and setters
}