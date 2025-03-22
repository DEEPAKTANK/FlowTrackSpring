package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.CreatePasswordRequest;
import com.deepak.proexpenditure.pro_expenditure.dto.LoginRequest;
import com.deepak.proexpenditure.pro_expenditure.service.AuthService;
import com.deepak.proexpenditure.pro_expenditure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    ;

    @PostMapping("/create_password")
    public ResponseEntity<String> createPassword(@RequestBody CreatePasswordRequest request) {
        authService.createPassword(request);
        return ResponseEntity.ok("Password set successfully. You can now log in.");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(token);
    }
}
