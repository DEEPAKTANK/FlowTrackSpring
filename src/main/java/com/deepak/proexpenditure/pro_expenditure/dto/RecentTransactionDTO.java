package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecentTransactionDTO {
    private String transaction_id;
    private String description;
    private Long amount;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String bankName;
}
