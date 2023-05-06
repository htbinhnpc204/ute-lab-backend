package com.nals.tf7.repository;

import com.nals.tf7.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository
    extends JpaRepository<Schedule, Long> {

    Page<Schedule> findAllByOrderByIdAsc(Pageable pageable);
}
