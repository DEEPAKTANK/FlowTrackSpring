package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Integer> {

    // Find bank details by user
    Optional<BankDetails> findByUser(User user);

    // Fetch only required bank details as DTO
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.BankDTO(b.bankId, b.bankName, b.balance, b.active) FROM BankDetails b")
    List<BankDTO> findAllBankDetailsAsDTO();
}
