package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<BankDetails, String> {

    // Get Bank by ID
    @Query("SELECT b FROM BankDetails b WHERE b.bankId = :bankId")
    Optional<BankDetails> findByBankId(@Param("bankId") String bankId);

    // Get Active Bank by ID
    @Query("SELECT b FROM BankDetails b WHERE b.bankId = :bankId AND b.active = true")
    Optional<BankDetails> findByBankIdAndActiveTrue(@Param("bankId") String bankId);

    // Get Inactive Bank by ID
    @Query("SELECT b FROM BankDetails b WHERE b.bankId = :bankId AND b.active = false")
    Optional<BankDetails> findByBankIdAndActiveFalse(@Param("bankId") String bankId);

    // Get All Active Banks
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.active = true")
    List<BankDTO> findByActiveTrue();

    // Get All Inactive Banks
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.active = false")
    List<BankDTO> findByActiveFalse();

    // Get Banks for a Specific User
    @Query("SELECT b FROM BankDetails b WHERE b.user.userId = :userId AND b.active = true")
    List<BankDetails> findByUserUserIdAndActiveTrue(@Param("userId") String userId);

    // Check if Bank Exists for User
    @Query("SELECT COUNT(b) > 0 FROM BankDetails b WHERE b.user.userId = :userId AND b.bankId = :bankId")
    boolean existsByUserIdAndBankId(@Param("userId") String userId, @Param("bankId") String bankId);

    long countByUser(User user);
    @Query("SELECT COALESCE(SUM(b.balance), 0) FROM BankDetails b WHERE b.user = :user")
    Long sumBalanceByUser(@Param("user") User user);
    /**
     * Fetch the initial balance of a bank by its ID.
     */
    @Query("SELECT b.balance FROM BankDetails b WHERE b.bankId = :bankId")
    Optional<Long> findInitialBalanceByBankId(@Param("bankId") String bankId);
    @Query("UPDATE BankDetails b SET b.balance = b.balance + :amount WHERE b.bankId = :bankId")
    void updateBalance(String bankId, Long amount);
}
