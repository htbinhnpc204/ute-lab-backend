package com.nals.tf7.repository;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.domain.Lab;
import com.nals.tf7.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ScheduleRepository
    extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByLab(Lab lab);

    List<Schedule> findAllByClassEntity(ClassEntity classEntity);

    List<Schedule> findAllByLabAndClassEntity(Lab lab, ClassEntity classEntity, Pageable pageable);

    Page<Schedule> findAllByClassEntityIn(Collection<ClassEntity> classEntity, Pageable pageable);
}
