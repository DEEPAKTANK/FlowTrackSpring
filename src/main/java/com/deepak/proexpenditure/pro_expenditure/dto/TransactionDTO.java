package com.deepak.proexpenditure.pro_expenditure.dto;

import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String transaction_id;
    private String description;
    private Long amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime transactionDate;
    private LocalDateTime updatedAt;
    private String bankId;
    public static TransactionDTO fromEntity(Transaction transaction ) {
        return TransactionDTO.builder()
                .transactionDate(transaction.getTransactionDate())
                .amount(transaction.getAmount())
                .transaction_id(transaction.getTransaction_id())
                .status(transaction.getStatus())
                .updatedAt(transaction.getUpdatedAt())
                .bankId(transaction.getBankDetails().getBankId())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())
                .build();
    }
}
