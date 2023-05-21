package com.nals.tf7.repository;

import com.nals.tf7.domain.ClassUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassUserRepository
    extends JpaRepository<ClassUser, Long> {

    Page<ClassUser> getAllByClassIdOrderByClassUserRoleAsc(Long classId, Pageable pageable);

    Optional<ClassUser> findByClassIdAndUserId(Long classId, Long userId);
}
