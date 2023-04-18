package com.nals.tf7.repository;

import com.nals.tf7.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository
    extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    @Query("SELECT DISTINCT p"
        + " FROM Permission p"
        + " JOIN RolePermission rp ON rp.permissionId = p.id"
        + " JOIN User u ON u.role.id = rp.roleId"
        + " WHERE u.id = :userId")
    Set<Permission> fetchByUserId(@Param("userId") Long userId);
}
