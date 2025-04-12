package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.*;
import com.deepak.proexpenditure.pro_expenditure.exception.AuthenticationException;
import com.deepak.proexpenditure.pro_expenditure.exception.EmailAlreadyExistsException;
import com.deepak.proexpenditure.pro_expenditure.exception.PhoneAlreadyExistsException;
import com.deepak.proexpenditure.pro_expenditure.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/create_password")
    public ResponseEntity<Map<String, String>> createPassword(@RequestBody CreatePasswordRequest request) {
        authService.createPassword(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password set successfully. You can now log in.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
            response=authService.authenticate(request);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication Failed");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/add_email")
    public ResponseEntity<String> addEmail(@RequestBody @Valid AddEmailRequest request) {

        try {
            authService.addEmail(request);
            return ResponseEntity.ok("Email added successfully.");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/add_phone")
    public ResponseEntity<?> addPhone(@RequestBody @Valid AddPhoneRequest request) {
        try {
            authService.addPhone(request);
            return ResponseEntity.ok("Phone number added successfully.");
        } catch (PhoneAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password reset verification initiated."));
    }
}
