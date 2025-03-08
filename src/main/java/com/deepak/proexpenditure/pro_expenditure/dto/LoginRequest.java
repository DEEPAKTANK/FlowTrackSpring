package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}