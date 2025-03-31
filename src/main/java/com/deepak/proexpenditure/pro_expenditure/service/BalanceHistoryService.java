package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.entity.BalanceHistory;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.events.TransactionCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.BalanceHistoryRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j

@AllArgsConstructor
@Service
public class BalanceHistoryService {
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BankRepository bankDetailsRepository;

    @Transactional
    @EventListener
    public void handleBankCreatedEvent(BankCreatedEvent event) {
        // Retrieve bank details from event
        BankDetails savedBank = bankDetailsRepository.findByBankId(event.getBankDetails().getBankId()).orElseThrow(() -> new IllegalArgumentException("Bank not found"));
        User user = event.getUser();  // Assuming event contains user info
        BalanceHistory balanceHistory = BalanceHistory.builder()
                .previousBalance((long) 0.0)
                .newBalance( event.getInitialBalance())
                .user(user)
                .bank(savedBank)
                .updatedAt(LocalDateTime.now())
                .updatedBy(event.getUser().getModifiedBy())
                .build();
        balanceHistoryRepository.save(balanceHistory);
    }

    @Transactional
    public void updateBalanceHistory(User user, BankDetails bank, Long previousBalance, Long newBalance, String updatedBy, Transaction transaction) {
        BalanceHistory balanceHistory = balanceHistoryRepository.findByBank(bank);
        balanceHistory.setNewBalance(newBalance);
        balanceHistory.setPreviousBalance(previousBalance);
        balanceHistory.setUpdatedBy(updatedBy);
        balanceHistory.setUpdatedAt(LocalDateTime.now());
        balanceHistory.setLastTransaction(transaction);
        balanceHistoryRepository.save(balanceHistory);
    }

    @Transactional
    public void updateBalanceHistoryForTransaction(TransactionCreatedEvent event) {
        BankDetails savedBank = bankDetailsRepository.findByBankId(event.getTransaction().getBankDetails().getBankId()).orElseThrow(() -> new IllegalArgumentException("Bank not found"));

        User user = event.getTransaction().getBankDetails().getUser();  // Assuming event contains user info
        Long amountChange = event.getTransaction().getTransactionType() == TransactionType.CREDIT ? event.getTransaction().getAmount()  // Increase balance for deposits
                : -event.getTransaction().getAmount();
        if (!event.isInitial()) {
            updateBalanceHistory(user, savedBank, event.getTransaction().getBankDetails().getBalance(), amountChange+event.getTransaction().getBankDetails().getBalance(), user.getModifiedBy(),event.getTransaction());
        }
    }
}
