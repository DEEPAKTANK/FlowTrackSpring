package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.BalanceHistoryDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BalanceHistory;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.BalanceHistoryRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.BankDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BankDetailsRepository bankDetailsRepository;

    @Transactional
    public BalanceHistoryDTO createBalanceHistory(User user, BankDetails bank, Long previousBalance, Long newBalance, String updatedBy) {
        BalanceHistory balanceHistory = BalanceHistory.builder()
                .previousBalance(previousBalance)
                .newBalance(newBalance)
                .user(user)
                .bank(bank)
                .updatedAt(LocalDateTime.now())
                .updatedBy(updatedBy)
                .build();

        balanceHistoryRepository.save(balanceHistory);
        return new BalanceHistoryDTO(balanceHistory.getUser().getUserId(),balanceHistory.getBank().getBankId(), previousBalance, newBalance, balanceHistory.getUpdatedAt(),updatedBy);
    }

    @Transactional
    @EventListener
    public void handleBankCreatedEvent(BankCreatedEvent event) {
        // Retrieve bank details from event
        BankDetails savedBank = bankDetailsRepository.findByBankId(event.getBankDetails().getBankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank not found"));

        User user = event.getUser();  // Assuming event contains user info

        createBalanceHistory(user, savedBank, event.getInitialBalance(), event.getInitialBalance(), event.getUser().getModifiedBy());
    }
}
