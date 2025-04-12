package com.deepak.proexpenditure.pro_expenditure.entity;

import com.deepak.proexpenditure.pro_expenditure.enums.TransactionType;
import com.deepak.proexpenditure.pro_expenditure.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class) // Enable auditing
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private BankDetails bankDetails;

    @NonNull
    @Column(name = "amount", nullable = false)
    private Long amount;
    @NonNull
    @Column(name = "transaction_id", nullable = false)
    private String transaction_id;

    @Column(name = "description")
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING; // Default to PENDING

    @NonNull
    @CreationTimestamp
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;

    @Nullable
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Nullable
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @NonNull
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
