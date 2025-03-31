package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.IsInitial;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.events.TransactionCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.exception.ResourceNotFoundException;
import com.deepak.proexpenditure.pro_expenditure.exception.UnauthorizedAccessException;
import com.deepak.proexpenditure.pro_expenditure.repository.BalanceHistoryRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.deepak.proexpenditure.pro_expenditure.enums.IsInitial.FALSE;
import static com.deepak.proexpenditure.pro_expenditure.enums.IsInitial.TRUE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher; // ✅ Injected
    private IsInitial isInitial=FALSE; // ✅ Injected


    // ✅ Get logged-in user ID
    private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // ✅ Create Bank Account (Only for Logged-in User)
    public BankDTO createBankAccount(BankDTO bankDTO) {
        String loggedInUserId = getLoggedInUserId();
        User user = userRepository.findByUserIdAndActiveTrue(loggedInUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Active user not found"));

        // Get count of existing banks for this user
        long bankCount = bankRepository.countByUser(user);

        // Generate structured bank ID
        String bankId = String.format("BNK-%s-%04d", loggedInUserId, bankCount + 1);
        isInitial = TRUE;
        BankDetails bankDetails = BankDetails.builder()
                .bankName(bankDTO.getBankName())
                .bankId(bankId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .accountType(bankDTO.getAccountType())
                .ifscCode(bankDTO.getIfscCode())
                .branchName(bankDTO.getBranchName())
                .balance(bankDTO.getBalance())
                .user(user)
                .active(true) // Default active
                .createdBy(loggedInUserId)
                .modifiedBy(loggedInUserId)
                .build();

        // Save bank details first
        BankDetails savedBank = bankRepository.save(bankDetails);

// Check if the user already has a TotalBalance entry
        applicationEventPublisher.publishEvent(new BankCreatedEvent(this, user, savedBank, bankDTO.getBalance()));
        return BankDTO.fromEntity(savedBank);
    }

    // ✅ Get Active Banks (Only for Logged-in User)
    public List<BankDTO> getActiveBanks() {
        String loggedInUserId = getLoggedInUserId();
        return bankRepository.findByUserUserIdAndActiveTrue(loggedInUserId)
                .stream()
                .map(BankDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ Get Inactive Banks (Only for Logged-in User)
//    public List<BankDTO> getInactiveBanks() {
//        String loggedInUserId = getLoggedInUserId();
//        return bankRepository.findByUserUserIdAndActiveFalse(loggedInUserId)
//                .stream()
//                .map(BankDTO::fromEntity)
//                .collect(Collectors.toList());
//    }

    // ✅ Get Bank by ID (Only Owner or Admin)
    public BankDTO getBankById(String bankId) {
        String loggedInUserId = getLoggedInUserId();
        BankDetails bank = bankRepository.findByBankIdAndActiveTrue(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Active bank not found"));

        if (!bank.getUser().getUserId().equals(loggedInUserId)) {
            throw new UnauthorizedAccessException("You are not authorized to access this bank account");
        }

        return BankDTO.fromEntity(bank);
    }

    // ✅ Update Bank Account (Only Owner)
    public BankDTO updateBankAccount(String bankId, BankDTO bankDTO) {
        String loggedInUserId = getLoggedInUserId();
        BankDetails bank = bankRepository.findByBankIdAndActiveTrue(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Active bank not found"));

        if (!bank.getUser().getUserId().equals(loggedInUserId)) {
            throw new UnauthorizedAccessException("You are not authorized to update this bank account");
        }

        bank.setBankName(bankDTO.getBankName());
        bank.setIfscCode(bankDTO.getIfscCode());
        bank.setBranchName(bankDTO.getBranchName());
        bank.setBalance(bankDTO.getBalance());
        bank.setAccountType(bankDTO.getAccountType());
        bank.setModifiedBy(loggedInUserId);

        BankDetails updatedBank = bankRepository.save(bank);
        return BankDTO.fromEntity(updatedBank);
    }

    // ✅ Soft Delete Bank (Only Owner)
    public void deactivateBank(String bankId) {
        String loggedInUserId = getLoggedInUserId();
        BankDetails bank = bankRepository.findByBankIdAndActiveTrue(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Active bank not found"));

        if (!bank.getUser().getUserId().equals(loggedInUserId)) {
            throw new UnauthorizedAccessException("You are not authorized to deactivate this bank account");
        }

        bank.setActive(false);
        bankRepository.save(bank);
    }

    // ✅ Restore Deleted Bank (Only Owner)
    public void restoreBank(String bankId) {
        String loggedInUserId = getLoggedInUserId();
        BankDetails bank = bankRepository.findByBankIdAndActiveFalse(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found"));

        if (!bank.getUser().getUserId().equals(loggedInUserId)) {
            throw new UnauthorizedAccessException("You are not authorized to restore this bank account");
        }

        bank.setActive(true);
        bankRepository.save(bank);
    }

    // ✅ Get Banks for a Specific User (Only if Logged-in User Matches)
    public List<BankDTO> getBanksByUserId(String userId) {
        String loggedInUserId = getLoggedInUserId();

        if (!loggedInUserId.equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to view this user's bank accounts");
        }

        return bankRepository.findByUserUserIdAndActiveTrue(userId)
                .stream()
                .map(BankDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBalance(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        BankDetails bankDetails = transaction.getBankDetails();

        if (bankDetails == null) {
            log.error("Bank details not found for transaction ID: {}", transaction.getTransaction_id());
            throw new ResourceNotFoundException("Active bank not found");
        }

        if (!event.isInitial()) {
            // Determine the amount update based on transaction type
            Long amountChange = transaction.getTransactionType() == TransactionType.CREDIT
                    ? transaction.getAmount()  // Increase balance for deposits
                    : -transaction.getAmount(); // Decrease balance for withdrawals

            bankDetails.setBalance(bankDetails.getBalance() + amountChange);
            bankRepository.save(bankDetails);

            log.info("✅ Updated bank balance for Bank ID: {} | New Balance: {}",
                    bankDetails.getBankId(), bankDetails.getBalance());
        }
    }
}
