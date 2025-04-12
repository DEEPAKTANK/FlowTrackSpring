package com.deepak.proexpenditure.pro_expenditure.security;

import com.deepak.proexpenditure.pro_expenditure.entity.*;
import com.deepak.proexpenditure.pro_expenditure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserEmailRepository userEmailRepository;
    private final UserPhoneRepository userPhoneRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> optionalUser;

        if (identifier.contains(".com")) { // ✅ Login via Email
            UserEmail userEmail = userEmailRepository.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + identifier));
            optionalUser = Optional.of(userEmail.getUser());

        } else if (identifier.matches("\\d+")) { // ✅ Login via Phone Number
            UserPhone userPhone = userPhoneRepository.findByPhone(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone number not found: " + identifier));
            optionalUser = Optional.of(userPhone.getUser());

        } else { // ✅ Login via User ID
            optionalUser = userRepository.findByUserIdAndActiveTrue(identifier);
        }

        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier));

        // ✅ Fetch authentication details
        UserAuth userAuth = userAuthRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Authentication details not found for user: " + identifier));

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                userAuth.getPasswordHash(),
                Collections.emptyList()
        );
    }
}
