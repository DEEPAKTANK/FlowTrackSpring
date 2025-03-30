package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.TotalBalance;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TotalBalanceRepository extends JpaRepository<TotalBalance, Integer> {
    Optional<TotalBalance> findByUser(User user);  // 🔥 Fix: Use User entity instead of userId
    Optional<TotalBalance> findByUserId(int userId);  // Fix method signature to use userId
}
