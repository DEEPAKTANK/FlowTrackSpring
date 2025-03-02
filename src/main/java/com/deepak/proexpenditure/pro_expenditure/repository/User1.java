package com.deepak.proexpenditure.pro_expenditure.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_credentials")
public class User1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment primary key
    private int id;
    @Column(name="userId",nullable = false, unique = true)
    private String userId;
    @PrePersist
    private void generateUserId() {
        this.userId = "emp_" + id;
    }
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username can only contain letters, numbers, and underscores.")
    @Column(name="userName",nullable = false, unique = true,length = 30)
    private String userName;
    @Column(name="userPassword",length = 16)
    private String userPassword;
    @Column(name="dateRegistered")
    @CreationTimestamp
    private LocalDateTime dateRegistered;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @Column(name="userEmail", unique = true,length = 255)
    private String userEmail;
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    @Column(name="phoneNumber", unique = true,length = 12)
    private String phoneNumber;
}
