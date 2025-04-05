package com.deepak.proexpenditure.pro_expenditure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AddEmailRequest {
    @NotBlank(message = "User ID is required")
    private String identifier;


    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    private String isPrimary;
}
