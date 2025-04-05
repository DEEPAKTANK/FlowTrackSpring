package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.entity.UserPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPhoneRepository extends JpaRepository<UserPhone, Integer> {
    List<UserPhone> findByUser_UserId(String userId); // ✅ Get all phone numbers for a user

    Optional<UserPhone> findByPhone(String phone); // ✅ Find by any phone number

    Optional<UserPhone> findByUser_UserIdAndIsPrimaryTrue(String userId); // ✅ Find primary phone number

    boolean existsByPhone(String phone); // ✅ Check if phone already exists
}
