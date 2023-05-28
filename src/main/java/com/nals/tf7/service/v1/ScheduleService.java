package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.domain.Schedule;
import com.nals.tf7.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ScheduleService
    extends BaseService<Schedule, ScheduleRepository> {

    public ScheduleService(final ScheduleRepository repository) {
        super(repository);
    }

    public Page<Schedule> searchByLab(final Lab lab, final PageRequest req) {
        return getRepository().findAllByLab(lab, req);
    }

    public Page<Schedule> searchSchedule(final PageRequest req) {
        return getRepository().findAll(req);
    }
}
