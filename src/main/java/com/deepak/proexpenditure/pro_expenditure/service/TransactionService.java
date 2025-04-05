package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import com.deepak.proexpenditure.pro_expenditure.events.TransactionCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.TransactionRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import com.deepak.proexpenditure.pro_expenditure.utils.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.deepak.proexpenditure.pro_expenditure.enums.TransactionType.CREDIT;

@Slf4j

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates an initial transaction when a bank is created.
     */
    @Transactional
    public TransactionDTO createInitialTransaction(BankDetails bankDetails) {
        bankRepository.findByBankId(bankDetails.getBankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank not found with id: " + bankDetails.getBankId()));
        Transaction transaction = Transaction.builder()
                .bankDetails(bankDetails)
                .amount(bankDetails.getBalance())
                .transaction_id(IDGenerator.generateId("TRN"))
                .transactionType(CREDIT)
                .transactionDate(LocalDateTime.now())
                .status(TransactionStatus.SUCCESS)
                .updatedAt(LocalDateTime.now())
                .description("Initial Transaction")
                .build();

        return createTransaction(transaction,true);
    }


    @Transactional
    public TransactionDTO createTransactionFromBank( TransactionDTO transactionDTO) {
        BankDetails bankDetails = bankRepository.findByBankId(transactionDTO.getBankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank not found with id: " + transactionDTO.getBankId()));
        Transaction transaction = Transaction.builder()
                .bankDetails(bankDetails)
                .amount(transactionDTO.getAmount())
                .transaction_id(IDGenerator.generateId("TRN"))
                .transactionType(transactionDTO.getTransactionType())
                .transactionDate(LocalDateTime.now())
                .status(TransactionStatus.SUCCESS)
                .updatedAt(LocalDateTime.now())
                .description(transactionDTO.getDescription())
                .build();
        return createTransaction(transaction, false);
    }

    /**
     * Creates a new transaction and publishes an event.
     */
    @Transactional
    public TransactionDTO createTransaction(Transaction transaction , boolean isinitial) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedTransaction,isinitial));

        return TransactionDTO.fromEntity(transaction);
    }

//    /**
//     * Fetch transactions for a specific user with pagination
//     */
//    public Page<Transaction> getTransactionsByUser(User user, Pageable pageable) {
//        return transactionRepository.findByUser(user, pageable);
//    }

    /**
     * Fetch transactions by type
     */
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByTransactionType(type);
    }

    /**
     * Fetch transactions within a date range
     */
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start, end);
    }

//    /**
//     * Fetch transactions as DTO projection
//     */
//    public List<TransactionDTO> getTransactionsByIntroduce local variable(String bank_Id) {
//        return transactionRepository.findTransactionsByBankId(bank_Id);
//    }
}