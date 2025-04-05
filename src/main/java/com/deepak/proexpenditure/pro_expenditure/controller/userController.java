package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.RegisterUserRequest;
import com.deepak.proexpenditure.pro_expenditure.dto.UserDTO;
import com.deepak.proexpenditure.pro_expenditure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Base path for user-related operations
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** Register a New User */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserRequest request) {
        UserDTO userDTO = userService.registerUser(request);
        return ResponseEntity.ok(userDTO);
    }

    /** Get User by ID */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Get All Active Users */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
