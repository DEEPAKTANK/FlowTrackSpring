package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceHistoryDTO {

    private int id;
    private int userId;
    private int bankId;
    private Long previousBalance;
    private Long newBalance;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
