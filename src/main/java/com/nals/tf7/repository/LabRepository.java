package com.nals.tf7.repository;

import com.nals.tf7.domain.Lab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository
    extends JpaRepository<Lab, Long> {
    Page<Lab> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
