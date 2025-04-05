package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistoryDTO {

    private String userId;
    private String bankId;
    private Long previousBalance;
    private Long newBalance;
    private LocalDateTime updatedAt;
    private String updatedBy;
}