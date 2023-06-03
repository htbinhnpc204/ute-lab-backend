package com.nals.tf7.service.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.domain.Lab;
import com.nals.tf7.domain.Schedule;
import com.nals.tf7.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ScheduleService
    extends BaseService<Schedule, ScheduleRepository> {

    public ScheduleService(final ScheduleRepository repository) {
        super(repository);
    }

    public List<Schedule> searchByLab(final Lab lab) {
        log.info("Search all schedule by Lab #{}", lab);
        return getRepository().findAllByLab(lab);
    }

    public List<Schedule> searchByClass(final ClassEntity classEntity) {
        log.info("Search all by class #{}", classEntity);
        return getRepository().findAllByClassEntity(classEntity);
    }

    public List<Schedule> searchByLabAndClass(final Lab lab, final ClassEntity classEntity, final PageRequest req) {
        log.info("Search all by lab #{} and class #{}", lab, classEntity);
        return getRepository().findAllByLabAndClassEntity(lab, classEntity, req);
    }

    public Page<Schedule> getAllByClassEntityIn(final Collection<ClassEntity> classEntities, final PageRequest req) {
        log.info("Search all by classes in #{} and class #{}", classEntities, req);
        return getRepository().findAllByClassEntityIn(classEntities, req);
    }

    public Page<Schedule> searchSchedule(final PageRequest req) {
        log.info("Search schedule by #{}", req);
        return getRepository().findAll(req);
    }
}
