package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.*;
import com.deepak.proexpenditure.pro_expenditure.entity.*;
import com.deepak.proexpenditure.pro_expenditure.exception.EmailAlreadyExistsException;
import com.deepak.proexpenditure.pro_expenditure.exception.PhoneAlreadyExistsException;
import com.deepak.proexpenditure.pro_expenditure.repository.*;
import com.deepak.proexpenditure.pro_expenditure.security.JwtProvider;
import com.deepak.proexpenditure.pro_expenditure.utils.HashingUtils;
import com.deepak.proexpenditure.pro_expenditure.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserEmailRepository userEmailRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * Create Password for a User (First-time setup or Reset)
     */
    @Transactional
    public void createPassword(CreatePasswordRequest request) {
        // ✅ Find User by User ID
        User user = userRepository.findByUserIdAndActiveTrue(request.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found for password setup: {}", request.getUserId());
                    return new UsernameNotFoundException("User not found: " + request.getUserId());
                });


        // ✅ Check if password is already set
        if (!userAuthRepository.existsByUserIdAndHasPassword(user.getUserId()).isBlank()) {
            log.warn("Password already set for user: {}", user.getUserId());
            throw new RuntimeException("Password is already set. Use login instead.");
        }
        Optional<UserAuth> existingUserAuth = userAuthRepository.findByUser_UserId(user.getUserId());
        // ✅ Save new password
        String salt = PasswordUtils.generateSalt();
        if (existingUserAuth.isPresent()) {
            // ✅ Update existing record
            UserAuth userAuth = existingUserAuth.get();
            userAuth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userAuth.setSalt(salt);
            userAuth.setLastPasswordChange(LocalDateTime.now());

            userAuthRepository.save(userAuth);
            log.info("Password updated successfully for user: {}", user.getUserId());
        } else {
            // ✅ Create new entry if not found
            UserAuth newUserAuth = new UserAuth();
            newUserAuth.setUser(user);
            newUserAuth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            newUserAuth.setSalt(salt);
            newUserAuth.setLastPasswordChange(LocalDateTime.now());

            userAuthRepository.save(newUserAuth);
            log.info("Password successfully set for user: {}", user.getUserId());
        }
    }

    /**
     * Authenticate user by Email, Phone, or User ID.
     */
    public Map<String, String> authenticate(LoginRequest request) {
        Optional<User> optionalUser;

        // ✅ Identify Login Type (Email, Phone, User ID)
        if (request.getIdentifier().contains(".com")) { // Email Login
            optionalUser = userEmailRepository.findByEmail(request.getIdentifier())
                    .map(UserEmail::getUser);
        } else if (request.getIdentifier().matches("\\d+")) { // Phone Login
            optionalUser = userPhoneRepository.findByPhone(request.getIdentifier())
                    .map(UserPhone::getUser);
        } else { // User ID Login
            optionalUser = userRepository.findByUserIdAndActiveTrue(request.getIdentifier());
        }

        User user = optionalUser.orElseThrow(() -> {
            log.warn("User not found: {}", request.getIdentifier());
            return new UsernameNotFoundException("User not found: " + request.getIdentifier());
        });

        // ✅ Fetch authentication details
        UserAuth userAuth = userAuthRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> {
                    log.warn("Authentication details missing for user: {}", user.getUserId());
                    return new UsernameNotFoundException("Authentication details not found for user: " + request.getIdentifier());
                });

        // ✅ Validate Password
        if (!passwordEncoder.matches(request.getPassword(), userAuth.getPasswordHash())) {
            log.warn("Invalid password attempt for user: {}", user.getUserId());
            throw new BadCredentialsException("Invalid password");
        }
        // ✅ Generate & Return JWT Token
        String token = jwtProvider.generateToken(user.getUserId());
        log.info("User {} logged in successfully. JWT issued.", user.getUserId());
        Map<String, String> response = new HashMap<>();
        response.put("token",token);
        response.put("User",user.getUser_name());
        response.put("User",user.getUserId());
        return response;
    }

    public void addEmail(AddEmailRequest request) {
        User user = userRepository.findByUserIdAndActiveTrue(request.getIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getIdentifier()));

        // Check if email already exists for the user
        if (userEmailRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered with another account.");
        }
        // Set primary email logic (only one primary email allowed)
        if (Boolean.parseBoolean(request.getIsPrimary())) {
            userEmailRepository.findByUser_UserIdAndIsPrimaryTrue(user.getUserId())
                    .ifPresent(existingPrimary -> {
                        existingPrimary.setPrimary(false);
                        userEmailRepository.save(existingPrimary);
                    });
        }
        // Save new email
        UserEmail userEmail = new UserEmail(user, request.getEmail(), Boolean.parseBoolean(request.getIsPrimary()));
        userEmailRepository.save(userEmail);
    }

    public void addPhone(AddPhoneRequest request) {
        User user = userRepository.findByUserIdAndActiveTrue(request.getIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getIdentifier()));

        // Check if phone already exists
        if (userPhoneRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number is already registered with another account.");
        }

        // Set primary phone logic (only one primary phone allowed)
        if (Boolean.parseBoolean(request.getIsPrimary())) {
            userPhoneRepository.findByUser_UserIdAndIsPrimaryTrue(user.getUserId())
                    .ifPresent(existingPrimary -> {
                        existingPrimary.setPrimary(false);
                        userPhoneRepository.save(existingPrimary);
                    });
        }

        // Save new phone number
        UserPhone userPhone = UserPhone.builder()
                .user(user)
                .phone(request.getPhone())
                .isPrimary(Boolean.parseBoolean(request.getIsPrimary()))
                .build();
        userPhoneRepository.save(userPhone);
    }
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // ✅ Validate input
        if (request.getUserName() == null || (request.getPhone() == null && request.getEmail() == null)) {
            throw new IllegalArgumentException("Username and either phone or email must be provided");
        }

        // ✅ Find user by username
        User user = userRepository.findByUserId(request.getUserName())
                .orElseThrow(() -> {
                    log.warn("Forgot password: User not found - {}", request.getUserName());
                    return new UsernameNotFoundException("User not found");
                });

        // ✅ Check either phone or email matches
        boolean match = false;
        List<UserPhone> phoneNumbers = userPhoneRepository.findByUser_UserId(user.getUserId());

        for (UserPhone phone : phoneNumbers) {
            if (request.getPhone() != null && request.getPhone().equals(phone.getPhone())) {
                match = true;
                break;
            }
        }

        if (!match) {
            log.warn("Forgot password: Email or phone mismatch for user {}", request.getUserName());
            throw new IllegalArgumentException("Provided email or phone does not match our records.");
        }

        // ✅ Proceed to reset flow - e.g., send OTP or temporary token (to be implemented)
        log.info("Forgot password initiated for user: {}", user.getUserId());
    }

}
