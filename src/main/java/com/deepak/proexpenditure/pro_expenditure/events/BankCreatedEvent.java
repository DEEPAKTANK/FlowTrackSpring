package com.deepak.proexpenditure.pro_expenditure.events;

import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import org.springframework.context.ApplicationEvent;

public class BankCreatedEvent extends ApplicationEvent {
    private final User user;
    private final BankDetails bankDetails;
    private final long initialBalance;

    public BankCreatedEvent(Object source, User user, BankDetails bankDetails, long initialBalance) {
        super(source);
        this.user = user;
        this.bankDetails = bankDetails;
        this.initialBalance = initialBalance;
    }

    public String getUserId() {
        return user.getUserId();
    }


    public long getInitialBalance() {  // ✅ Add this method to retrieve balance
        return initialBalance;
    }
}
