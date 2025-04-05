package com.deepak.proexpenditure.pro_expenditure.entity;

import com.deepak.proexpenditure.pro_expenditure.enums.UserRole;
import com.deepak.proexpenditure.pro_expenditure.enums.UserStatus;
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
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class) // Enable auditing
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private String userId;

    @NonNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @NonNull
    @Column(name = "user_name", nullable = false, length = 16,unique = true)
    private String user_name;


    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER; // Default USER

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // Default ACTIVE

    @Nullable
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "active", nullable = false)
    private boolean active = true; // Default to active user

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

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankDetails> bankAccounts;

    @PrePersist
    private void generateUserId() {
        this.userId = user_name;
    }
    @NonNull
    @CreationTimestamp
    @Column(name = "date_registered", updatable = false)
    private LocalDateTime dateRegistered;

}
