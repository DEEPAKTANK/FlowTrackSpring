package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.dto.DashboardSummary;
import com.deepak.proexpenditure.pro_expenditure.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary(Authentication authentication) {
        try {
            log.info(String.valueOf(authentication));
            String loggedInUserId = authentication.getName(); // Get logged-in user's ID

            // ✅ Extract userId from token (assumes token is parsed earlier via a filter)
            if (loggedInUserId == null) {
                throw new IllegalAccessException("Unauthorized: Token missing or invalid");
            }

            DashboardSummary summary = dashboardService.getDashboardSummary(loggedInUserId);
            return ResponseEntity.ok(summary);

        } catch (IllegalAccessException e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching dashboard data");
        }
    }
}
