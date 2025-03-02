package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String name;
    private UserStatus status;
    private boolean active;
}
