package com.nals.tf7.security;

import com.nals.tf7.helpers.SecurityHelper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.nals.tf7.config.Constants.SYSTEM;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware
    implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = SecurityHelper.getCurrentUserLogin().orElse(SYSTEM);
        return Optional.of(username);
    }
}
