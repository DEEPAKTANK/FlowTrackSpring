package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.TotalBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TotalBalanceRepository extends JpaRepository<TotalBalance, Integer> {
    Optional<TotalBalance> findByUserId(int userId);
}
