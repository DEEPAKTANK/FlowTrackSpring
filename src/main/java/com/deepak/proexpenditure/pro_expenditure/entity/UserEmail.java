package com.deepak.proexpenditure.pro_expenditure.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary; // ✅ Ensure this field exists

    public UserEmail(User user, String email, boolean isPrimary) {
        this.user = user;
        this.email = email;
        this.isPrimary = isPrimary;
    }
}