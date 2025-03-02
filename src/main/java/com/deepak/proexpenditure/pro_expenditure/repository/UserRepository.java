package com.deepak.proexpenditure.pro_expenditure.repository;

import com.deepak.proexpenditure.pro_expenditure.dto.UserDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    // Find user by ID
    Optional<User> findByUserId(String userId);

    // Find all active users with pagination
    Page<User> findByActiveTrue(Pageable pageable);

    // Find users by status
    List<User> findByStatus(UserStatus status);

    // Fetch users as DTO to avoid exposing full entity
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.UserDTO(u.userId, u.name, u.status, u.active) FROM User u")
    List<UserDTO> findAllUsersAsDTO();
}
