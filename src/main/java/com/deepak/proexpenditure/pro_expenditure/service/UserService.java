package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.UserDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** Create a New User */
    @Transactional
    public UserDTO createUser(User user) {
        User savedUser = userRepository.save(user);
        return new UserDTO(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getStatus(),
                savedUser.isActive(),
                savedUser.getDateRegistered()
        );
    }

    /** Get Active User by ID */
    public Optional<UserDTO> getUserById(String userId) {
        return userRepository.findByUserIdAndActiveTrue(userId)
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getRole(),
                        user.getStatus(),
                        user.isActive(),
                        user.getDateRegistered()
                ));
    }

    /** Get All Active Users */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByActiveTrue()
                .stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getRole(),
                        user.getStatus(),
                        user.isActive(),
                        user.getDateRegistered()
                ))
                .collect(Collectors.toList());
    }

    /** Soft Delete (Deactivate User) */
    @Transactional
    public boolean deleteUser(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }

    /** Restore User */
    @Transactional
    public boolean restoreUser(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> {
                    user.setActive(true);
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }
}
