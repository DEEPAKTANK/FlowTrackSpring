package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.AccountType;
import com.deepak.proexpenditure.pro_expenditure.enums.UserRole;
import com.deepak.proexpenditure.pro_expenditure.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDTO {
    private String userId;
    private String name;
    private UserRole role;
    private UserStatus status;
    private boolean active;
    private LocalDateTime dateRegistered;

    // Constructor to convert User entity to DTO
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.active = user.isActive();
        this.dateRegistered = user.getDateRegistered();
    }
}
