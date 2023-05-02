package com.nals.tf7.repository;

import com.nals.tf7.domain.Computer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerRepository
    extends JpaRepository<Computer, Long> {
    Page<Computer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
