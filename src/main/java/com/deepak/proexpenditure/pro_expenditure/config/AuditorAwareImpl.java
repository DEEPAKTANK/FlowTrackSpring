package com.deepak.proexpenditure.pro_expenditure.config;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // You can integrate this with Spring Security for actual username
        return Optional.of("system_user"); // Placeholder value
    }
}