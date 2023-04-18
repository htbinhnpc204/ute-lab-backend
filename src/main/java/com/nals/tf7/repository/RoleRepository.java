package com.nals.tf7.repository;

import com.nals.tf7.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository
    extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Query("SELECT r"
        + " FROM Role r JOIN User u ON u.role.id = r.id"
        + " WHERE u.id = :userId")
    Set<Role> fetchByUserId(@Param("userId") Long userId);
}
