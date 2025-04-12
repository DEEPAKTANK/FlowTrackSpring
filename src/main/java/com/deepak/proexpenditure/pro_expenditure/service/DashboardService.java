package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.BankSummaryDTO;
import com.deepak.proexpenditure.pro_expenditure.dto.DashboardSummary;
import com.deepak.proexpenditure.pro_expenditure.dto.RecentTransactionDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.TransactionRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final TransactionRepository transactionRepository;

    public DashboardSummary getDashboardSummary(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> bankIds = userRepository.findAllBankIdsByUserId(user.getUserId());
        List<BankDetails> bankList = bankRepository.findAllByBankIds(bankIds);

        Long totalBalance = bankList.stream()
                .mapToLong(BankDetails::getBalance)
                .sum();

        List<Transaction> transactions = transactionRepository.findAllByBankDetails_BankIdInOrderByTransactionDateDesc(bankIds);

        List<RecentTransactionDTO> recentTransactions = transactions.stream()
                .limit(5)
                .map(txn -> new RecentTransactionDTO(txn.getTransaction_id(),txn.getDescription(), txn.getAmount(), txn.getTransactionType(), txn.getTransactionDate(),txn.getBankDetails().getBankName()))
                .toList();

        List<BankSummaryDTO> banks = bankList.stream()
                .map(bank -> new BankSummaryDTO(bank.getBankId(), bank.getBankName(), bank.getBalance()))
                .toList();

        return new DashboardSummary(
                user.getUserId(),
                user.getUser_name(),
                totalBalance,
                transactions.size(),
                recentTransactions,
                banks
        );
    }
}
