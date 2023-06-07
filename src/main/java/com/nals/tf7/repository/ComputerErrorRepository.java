package com.nals.tf7.repository;

import com.nals.tf7.domain.Computer;
import com.nals.tf7.domain.ComputerError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerErrorRepository
    extends JpaRepository<ComputerError, Long> {

    List<ComputerError> getAllByComputer(Computer computer);
}
