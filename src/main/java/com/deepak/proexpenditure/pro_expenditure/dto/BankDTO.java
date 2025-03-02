package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO {
    private String bankId;
    private String bankName;
    private long balance;
    private boolean active;
}
