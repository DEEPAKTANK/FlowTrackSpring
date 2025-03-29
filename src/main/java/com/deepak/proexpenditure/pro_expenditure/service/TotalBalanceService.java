package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.TotalBalanceDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.TotalBalance;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.CurrencyType;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.TotalBalanceRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TotalBalanceService {
    private final TotalBalanceRepository totalBalanceRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public TotalBalanceService(TotalBalanceRepository totalBalanceRepository, UserRepository userRepository, BankRepository bankRepository) {
        this.totalBalanceRepository = totalBalanceRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    @Transactional
    public TotalBalanceDTO createTotalBalance(String userId, Long initialBalance) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));


        if (totalBalanceRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("TotalBalance already exists for user: " + user.getId());
        }

        TotalBalance totalBalance = TotalBalance.builder()
                .user(user)
                .totalBalance(initialBalance)
                .currency(CurrencyType.INR) // Default to INR
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TotalBalance savedTotalBalance = totalBalanceRepository.save(totalBalance);
        return new TotalBalanceDTO(savedTotalBalance);
    }
    @Transactional
    @EventListener
    public void handleBankCreatedEvent(BankCreatedEvent event) {
        createTotalBalance(event.getUserId(), event.getInitialBalance());
    }

}
