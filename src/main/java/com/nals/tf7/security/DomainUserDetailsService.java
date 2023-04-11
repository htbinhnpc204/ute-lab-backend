package com.nals.tf7.security;

import com.nals.tf7.domain.Permission;
import com.nals.tf7.domain.Role;
import com.nals.tf7.domain.User;
import com.nals.tf7.repository.PermissionRepository;
import com.nals.tf7.repository.RoleRepository;
import com.nals.tf7.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Slf4j
@AllArgsConstructor
@Component("userDetailsService")
public class DomainUserDetailsService
    implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        log.info("Authenticating {}", username);

        return userRepository.findOneByUsername(username)
                             .map(this::createSpringSecurityUser)
                             .orElseThrow(() -> new UsernameNotFoundException(
                                 "Account " + username + " was not found in the database"));
    }

    private DomainUserDetails createSpringSecurityUser(final User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("Account " + user.getEmail() + " was not activated");
        }

        Long userId = user.getId();

        Set<GrantedAuthority> permissions = permissionRepository.fetchByUserId(userId)
                                                                .stream()
                                                                .map(Permission::getName)
                                                                .map(SimpleGrantedAuthority::new)
                                                                .collect(Collectors.toSet());

        Set<GrantedAuthority> roles = roleRepository.fetchByUserId(userId)
                                                    .stream()
                                                    .map(Role::getName)
                                                    .map(SimpleGrantedAuthority::new)
                                                    .collect(Collectors.toSet());

        return DomainUserDetails.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(roles)
                                .authorities(permissions)
                                .build();
    }
}
