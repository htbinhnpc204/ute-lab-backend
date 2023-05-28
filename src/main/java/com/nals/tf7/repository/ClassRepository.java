package com.nals.tf7.repository;

import com.nals.tf7.domain.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository
    extends JpaRepository<ClassEntity, Long> {

    boolean existsByName(String name);

    Page<ClassEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
