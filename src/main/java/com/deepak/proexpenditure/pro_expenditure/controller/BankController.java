package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    // ✅ Ensure logged-in user is assigned to the bank account
    @PostMapping("/createbank")
    public ResponseEntity<BankDTO> createBankAccount(@RequestBody BankDTO bankDTO, Authentication authentication) {
        String loggedInUserId = authentication.getName(); // Get logged-in user's ID
        log.info("loggedInUserId {}", loggedInUserId);
        bankDTO.setUserId(loggedInUserId); // Assign the logged-in user as the owner
        BankDTO createdBank = bankService.createBankAccount(bankDTO);
        return ResponseEntity.ok(createdBank);
    }

    // ✅ Restrict access to only the owner or an admin
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBanksByUserId(@PathVariable String userId, Authentication authentication) {
        String loggedInUserId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!loggedInUserId.equals(userId) && !isAdmin) {
            return ResponseEntity.status(403).body("Access denied: You are not authorized to view these bank accounts.");
        }
        return ResponseEntity.ok(bankService.getBanksByUserId(userId));
    }
}
