package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping("/createbank")
    public ResponseEntity<BankDTO> createBankAccount(@RequestBody BankDTO bankDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName(); // Get the logged-in user's ID
        BankDTO createdBank = bankService.createBankAccount(loggedInUserId, bankDTO);
        return ResponseEntity.ok(createdBank);
    }

    @PreAuthorize("#userId == authentication.principal.username or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankDTO>> getBanksByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(bankService.getBanksByUserId(userId));
    }

}