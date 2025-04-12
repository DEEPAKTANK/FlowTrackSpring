package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // ✅ Fetch transactions for a specific user with pagination
//    Page<Transaction> findByUser(User user, Pageable pageable);

    // ✅ Fetch transactions by type
    List<Transaction> findByTransactionType(TransactionType type);

    // ✅ Fetch transactions within a date range
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    // ✅ Fetch transactions with DTO projection (Fixed Query)
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO(t.transaction_id, t.amount, t.transactionType, t.status, t.transactionDate, t.updatedAt, t.bankDetails) FROM Transaction t WHERE t.bankDetails.bankId = :bankId")
    List<TransactionDTO> findTransactionsByBankId(@Param("bankId") String bankId);
//    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.TransactionDTO(t.transaction_id, t.amount, t.transactionType, t.status, t.transactionDate, t.updatedAt, t.bankDetails) " +
//            "FROM Transaction t WHERE t.bankDetails.bankId = :bankId " +
//            "ORDER BY t.transaction_id DESC LIMIT 1")
//    Optional<TransactionDTO> findLatestTransactionByBankId(@Param("bankId") String bankId);
List<Transaction> findAllByBankDetails_BankIdInOrderByTransactionDateDesc(List<String> bankIds);

}