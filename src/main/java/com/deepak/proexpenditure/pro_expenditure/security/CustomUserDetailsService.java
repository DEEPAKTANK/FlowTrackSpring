package com.deepak.proexpenditure.pro_expenditure.security;

import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.entity.UserAuth;
import com.deepak.proexpenditure.pro_expenditure.repository.UserAuthRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // Fetch User entity
        User user = userRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // Fetch authentication details
        UserAuth userAuth = userAuthRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Authentication details not found for user ID: " + userId));

        // Return a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(), // Username (userId in our case)
                userAuth.getPasswordHash(), // Password (hashed)
                Collections.emptyList() // Authorities (empty for now)
        );
    }
}
