package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, Long> { // ✅ Use Long if ID is Long
    Optional<UserEmail> findByEmail(String email);

    Optional<UserEmail> findByUser_UserIdAndIsPrimaryTrue(String userId);

    boolean existsByEmail(String email); // ✅ Add this to prevent duplicates
}
