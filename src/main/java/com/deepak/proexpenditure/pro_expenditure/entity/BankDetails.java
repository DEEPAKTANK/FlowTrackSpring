package com.deepak.proexpenditure.pro_expenditure.entity;

import com.deepak.proexpenditure.pro_expenditure.enums.AccountType;
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
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bank_details")
@EntityListeners(AuditingEntityListener.class) // Enable auditing
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bank_id", nullable = false, unique = true, updatable = false)
    private String bankId;

    @NonNull
    @Column(name = "bank_name", nullable = false, length = 50)
    private String bankName;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType = AccountType.SAVINGS; // Default SAVINGS

    @Nullable
    @Column(name = "ifsc_code",  length = 11)
    private String ifscCode;

    @Nullable
    @Column(name = "branch_name",  length = 100)
    private String branchName;

    @NonNull
    @Column(name = "balance", nullable = false)
    private long balance;

    @Column(name = "active", nullable = false)
    private boolean active = true; // Default to true

    @Nullable
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Nullable
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @NonNull
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NonNull
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
