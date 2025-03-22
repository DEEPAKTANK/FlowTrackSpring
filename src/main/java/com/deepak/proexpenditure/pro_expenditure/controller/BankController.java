package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    // ✅ Only the logged-in user can create a bank account
    @PostMapping("/create")
    public ResponseEntity<BankDTO> createBankAccount(@RequestBody BankDTO bankDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName(); // Get logged-in user's ID

        BankDTO createdBank = bankService.createBankAccount(bankDTO);
        return ResponseEntity.ok(createdBank);
    }

    // ✅ Only the owner or an admin can access the user's banks
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankDTO>> getBanksByUserId(@PathVariable String userId, Authentication authentication) {
        String loggedInUserId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!loggedInUserId.equals(userId) && !isAdmin) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        return ResponseEntity.ok(bankService.getBanksByUserId(userId));
    }
}
