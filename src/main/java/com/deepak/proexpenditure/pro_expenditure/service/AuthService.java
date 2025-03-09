package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.CreatePasswordRequest;
import com.deepak.proexpenditure.pro_expenditure.dto.LoginRequest;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.entity.UserAuth;
import com.deepak.proexpenditure.pro_expenditure.exception.ResourceNotFoundException;
import com.deepak.proexpenditure.pro_expenditure.repository.UserAuthRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Creates a password for the user and stores a hashed version securely.
     */
    public void createPassword(CreatePasswordRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch existing UserAuth or create a new one
        UserAuth userAuth = userAuthRepository.findByUser_UserId(request.getUserId())
                .orElse(new UserAuth());

        // Set values
        userAuth.setUser(user);  // Ensure it stays linked to the correct user
        userAuth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userAuth.setSalt("random-salt"); // You can generate a dynamic salt here
        userAuth.setLastPasswordChange(LocalDateTime.now());

        userAuthRepository.save(userAuth);  // This will update instead of inserting a new entry
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     */
    public String authenticate(LoginRequest request) {
        UserAuth userAuth = userAuthRepository.findByUser_UserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), userAuth.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Extra JWT claims (optional, can store user roles, ID, etc.)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userAuth.getUser().getUserId());
        claims.put("name", userAuth.getUser().getName());

        return jwtService.generateToken(claims,request.getUserId());
    }
}
