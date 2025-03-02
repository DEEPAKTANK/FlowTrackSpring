package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<BankDetails, Integer> {

    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.bankId = :bankId")
    Optional<BankDTO> findByBankId(@Param("bankId") String bankId);

    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.bankId = :bankId AND b.active = true")
    Optional<BankDTO> findByBankIdAndActiveTrue(@Param("bankId") String bankId);

    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.active = true")
    List<BankDTO> findByActiveTrue();

    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.accountType, b.ifscCode, b.branchName, b.balance, b.active, b.createdBy, b.modifiedBy, b.createdAt, b.updatedAt) FROM BankDetails b WHERE b.active = false")
    List<BankDTO> findByActiveFalse();
}
