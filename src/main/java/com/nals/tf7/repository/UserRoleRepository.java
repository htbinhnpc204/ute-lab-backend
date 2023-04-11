package com.nals.tf7.repository;

import com.nals.tf7.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository
    extends JpaRepository<UserRole, Long> {
}
