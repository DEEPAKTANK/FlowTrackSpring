package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankSummaryDTO {
    private String bankId;
    private String bankName;
    private Long balance;
}
