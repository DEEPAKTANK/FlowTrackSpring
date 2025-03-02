package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDTO {
    private String bankId;
    private String bankName;
    private AccountType accountType;
    private String ifscCode;
    private String branchName;
    private long balance;
    private boolean active;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static method to convert entity to DTO
    public static BankDTO fromEntity(BankDetails bankDetails) {
        return BankDTO.builder()
                .bankId(bankDetails.getBankId())
                .bankName(bankDetails.getBankName())
                .accountType(bankDetails.getAccountType())
                .ifscCode(bankDetails.getIfscCode())
                .branchName(bankDetails.getBranchName())
                .balance(bankDetails.getBalance())
                .active(bankDetails.isActive())
                .createdBy(bankDetails.getCreatedBy())
                .modifiedBy(bankDetails.getModifiedBy())
                .createdAt(bankDetails.getCreatedAt())
                .updatedAt(bankDetails.getUpdatedAt())
                .build();
    }
}
