package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {

    // Find UserAuth by User ID
    Optional<UserAuth> findByUser_UserId(String userId);
    // ✅ Check if user has a non-null password
    @Modifying
    @Transactional
    @Query("UPDATE UserAuth ua SET ua.passwordHash = :passwordHash, ua.salt = :salt, ua.lastPasswordChange = :lastPasswordChange WHERE ua.user.userId = :userId")
    int updateUserAuth(
            @Param("userId") String userId,
            @Param("passwordHash") String passwordHash,
            @Param("salt") String salt,
            @Param("lastPasswordChange") LocalDateTime lastPasswordChange
    );
    @Query("SELECT passwordHash FROM UserAuth u WHERE u.user.userId = :userId")
    String existsByUserIdAndHasPassword(@Param("userId") String userId);
}
