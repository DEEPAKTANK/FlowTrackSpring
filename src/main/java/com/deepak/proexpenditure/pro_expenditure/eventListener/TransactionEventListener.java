package com.deepak.proexpenditure.pro_expenditure.eventListener;

import com.deepak.proexpenditure.pro_expenditure.entity.BalanceHistory;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import com.deepak.proexpenditure.pro_expenditure.events.BankCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.events.TransactionCreatedEvent;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import com.deepak.proexpenditure.pro_expenditure.service.BalanceHistoryService;
import com.deepak.proexpenditure.pro_expenditure.service.BankService;
import com.deepak.proexpenditure.pro_expenditure.service.TotalBalanceService;
import com.deepak.proexpenditure.pro_expenditure.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {
    private final TotalBalanceService totalBalanceService;
    private final TransactionService transactionService;
    private final BalanceHistoryService balanceHistoryService;
    private final BankService bankService;
    @Autowired
    private BankRepository bankDetailsRepository;
    @EventListener
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        log.info("Updating total balance for user: {}", transaction.getBankDetails().getUser().getUser_name());
        balanceHistoryService.updateBalanceHistoryForTransaction(event);
        bankService.updateBalance(event);
        totalBalanceService.updateTotalBalance(transaction.getBankDetails().getUser());

    }

    @EventListener
    public void handleBankCreated(BankCreatedEvent event) {
        log.info("Creating initial transaction for bank: {}", event.getUserId());
        transactionService.createInitialTransaction(event.getBankDetails()) ;
    }

}
