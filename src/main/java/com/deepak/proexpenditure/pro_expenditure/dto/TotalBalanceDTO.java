package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.entity.TotalBalance;
import com.deepak.proexpenditure.pro_expenditure.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalBalanceDTO {
    private String userId;
    private Long totalBalance;
    private CurrencyType currency;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Add this constructor
    public TotalBalanceDTO(TotalBalance totalBalance) {
        this.userId = totalBalance.getUser().getUserId();
        this.totalBalance = totalBalance.getTotalBalance();
        this.currency = totalBalance.getCurrency();
        this.active = totalBalance.isActive();
        this.createdAt = totalBalance.getCreatedAt();
        this.updatedAt = totalBalance.getUpdatedAt();
    }
}
