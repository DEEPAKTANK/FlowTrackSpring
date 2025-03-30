package com.deepak.proexpenditure.pro_expenditure.entity;

import com.deepak.proexpenditure.pro_expenditure.enums.CurrencyType;
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
@Table(name = "total_balance", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
@EntityListeners(AuditingEntityListener.class) // Enable auditing
public class TotalBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 10)
    private CurrencyType currency = CurrencyType.INR; // Default currency INR

    @NonNull
    @Column(name = "total_balance", nullable = false, precision = 18, scale = 2,updatable = true)
    private Long totalBalance;

    @Nullable
    @OneToOne
    @JoinColumn(name = "last_transaction_id", unique = true)
    private Transaction lastTransaction;

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
