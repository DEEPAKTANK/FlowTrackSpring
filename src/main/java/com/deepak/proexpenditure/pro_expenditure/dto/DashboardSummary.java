package com.deepak.proexpenditure.pro_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardSummary {
    private String userId;
    private String userName;
    private Long totalBalance;
    private int totalTransactions;
    private List<RecentTransactionDTO> recentTransactions;
    private List<BankSummaryDTO> banks;
}

