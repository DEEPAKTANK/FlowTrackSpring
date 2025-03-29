package com.deepak.proexpenditure.pro_expenditure.mapper;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {
    public BankDTO toDTO(BankDetails bankDetails) {
        return BankDTO.builder()

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
