package com.deepak.proexpenditure.pro_expenditure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class AddPhoneRequest {
    @NotBlank(message = "User ID is required")
    private String identifier;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Invalid phone number format")
    private String phone;

    private String isPrimary;
}
