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

    /**
     * Save a User (inherited from JpaRepository)
     */
    User save(User user);

    /**
     * Find User by User ID
     */
    Optional<User> findByUserId(String userId);

    /**
     * Find Active User by User ID and return DTO
     */
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.UserDTO(u.userId, u.name, u.role, u.status, u.active, u.dateRegistered) " +
            "FROM User u WHERE u.userId = :userId AND u.active = true")
    Optional<User> findByUserIdAndActiveTrue(String userId);

    /**
     * Get All Active Users as DTOs
     */
    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.UserDTO(u.userId, u.name, u.role, u.status, u.active, u.dateRegistered) " +
            "FROM User u WHERE u.active = true")
    List<UserDTO> findAllByActiveTrue();

    @Query("SELECT new com.deepak.proexpenditure.pro_expenditure.dto.UserDTO(u.userId, u.name, u.role, u.status, u.active, u.dateRegistered) FROM User u")
    List<UserDTO> findUsers();
}