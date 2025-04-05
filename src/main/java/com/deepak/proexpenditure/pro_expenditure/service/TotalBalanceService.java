package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.TotalBalanceDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.TotalBalance;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.CurrencyType;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.TotalBalanceRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.TransactionRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j

@AllArgsConstructor
@Service
public class TotalBalanceService {
    private final TotalBalanceRepository totalBalanceRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;


    @Transactional
    public TotalBalanceDTO createTotalBalance(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return updateTotalBalance(user);
    }

    @Transactional
    @EventListener
    public void handleBankCreatedEvent(BankCreatedEvent event) {
        createTotalBalance(event.getUserId());
    }

    @Transactional
    public TotalBalanceDTO
    updateTotalBalance(User user) {
        // Get the sum of balances for all the user's banks
        Long totalBalanceAmount = bankRepository.sumBalanceByUser(user);
        totalBalanceAmount = (totalBalanceAmount != null) ? totalBalanceAmount : 0L;

        log.info("Total Balance Amount for User [{}]: {}", user.getId(), totalBalanceAmount);

        // Find existing TotalBalance entry using userId
        Optional<TotalBalance> totalBalanceOpt = totalBalanceRepository.findByUserId(user.getId());

        if (totalBalanceOpt.isPresent()) {
            TotalBalance totalBalance = totalBalanceOpt.get();
            totalBalance.setTotalBalance(totalBalanceAmount);
            totalBalanceRepository.save(totalBalance);
            TotalBalance savedTotalBalance = totalBalanceRepository.save(totalBalance);
            log.info("Updated TotalBalance for User [{}]: {}", user.getId(), totalBalanceAmount);
            return new TotalBalanceDTO(savedTotalBalance);
        } else {
            // ✅ Create new TotalBalance record only if it doesn't exist
            TotalBalance totalBalance = TotalBalance.builder()
                    .user(user)
                    .totalBalance(totalBalanceAmount)
                    .currency(CurrencyType.INR) // Set default currency
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .createdBy(user.getUserId())
                    .modifiedBy(user.getUserId())
                    .active(true) // Set active by default
                    .build();
            TotalBalance savedTotalBalance = totalBalanceRepository.save(totalBalance);
            log.info("Created new TotalBalance for User [{}]: {}", user.getId(), totalBalanceAmount);
            return new TotalBalanceDTO(savedTotalBalance);
        }
    }
}
