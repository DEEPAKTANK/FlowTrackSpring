package com.deepak.proexpenditure.pro_expenditure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "password_hash", nullable = false, length = 255,unique = false)
    private String passwordHash;

    @Column(name = "salt", length = 100, nullable = true) // Nullable in case of Bcrypt
    private String salt;

    @Nullable
    @UpdateTimestamp
    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

}
