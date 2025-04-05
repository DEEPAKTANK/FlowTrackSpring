package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.BalanceHistory;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Integer> {

    BalanceHistory findByBank(BankDetails bank);
}
