package com.deepak.proexpenditure.pro_expenditure.events;

import com.deepak.proexpenditure.pro_expenditure.entity.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionCreatedEvent extends ApplicationEvent {
    private final Transaction transaction;
    private final boolean isInitial;

    public TransactionCreatedEvent(Object source, Transaction transaction, boolean isInitial) {
        super(source);
        this.transaction = transaction;
        this.isInitial = isInitial;
    }
    public Transaction getTransaction() {
        return transaction;
    }
}
