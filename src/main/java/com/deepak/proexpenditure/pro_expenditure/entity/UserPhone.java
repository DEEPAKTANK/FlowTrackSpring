package com.deepak.proexpenditure.pro_expenditure.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_phone")
public class UserPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phone;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;
}
