package com.deepak.proexpenditure.pro_expenditure.mapper;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {
    public BankDTO toDTO(BankDetails bankDetails) {
        return new BankDTO(
                bankDetails.getBankId(),
                bankDetails.getBankName(),
                bankDetails.getAccountType(),
                bankDetails.getIfscCode(),
                bankDetails.getBranchName(),
                bankDetails.getBalance(),
                bankDetails.isActive(),
                bankDetails.getCreatedBy(),
                bankDetails.getModifiedBy(),
                bankDetails.getCreatedAt(),
                bankDetails.getUpdatedAt()
        );
    }
}
