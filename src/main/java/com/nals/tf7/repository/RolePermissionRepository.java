package com.nals.tf7.repository;

import com.nals.tf7.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository
    extends JpaRepository<RolePermission, Long> {
}
