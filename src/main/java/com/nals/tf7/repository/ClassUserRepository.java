package com.nals.tf7.repository;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.domain.ClassUser;
import com.nals.tf7.domain.User;
import com.nals.tf7.enums.ClassUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassUserRepository
    extends JpaRepository<ClassUser, Long> {

    Page<ClassUser> getAllByClassEntityOrderByRoleAsc(ClassEntity classEntity, Pageable pageable);

    Optional<ClassUser> findByClassEntityAndUser(ClassEntity classEntity, User user);

    Optional<ClassUser> findByClassEntityAndRole(ClassEntity classEntity, ClassUserRole role);
}
