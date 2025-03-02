package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Integer> {

    // Fetch transactions for a specific user with pagination
    Page<Transaction> findByUser(User user, Pageable pageable);

    // Fetch transactions by type
    List<Transaction> findByTransactionType(TransactionType type);

    // Fetch transactions within a date range
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    // Fetch transactions with DTO projection
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO(t.id, t.amount, t.transactionType, t.status, t.transactionDate) FROM Transaction t WHERE t.user.userId = :userId")
    List<TransactionDTO> findTransactionsByUserId(String userId);
}
