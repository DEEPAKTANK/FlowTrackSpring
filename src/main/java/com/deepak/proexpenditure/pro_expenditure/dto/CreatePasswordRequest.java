package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.Data;

@Data
public class CreatePasswordRequest {
    private String userId;
    private String password;
}
