package com.nals.tf7.bloc.v1;

import com.nals.tf7.dto.v1.request.ScheduleReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.ScheduleRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.mapper.v1.ScheduleMapper;
import com.nals.tf7.service.v1.ClassService;
import com.nals.tf7.service.v1.LabService;
import com.nals.tf7.service.v1.ScheduleService;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nals.tf7.bloc.v1.ClassBloc.CLASS_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.LAB_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScheduleBloc {
    public static final String SCHEDULE_NOT_FOUND = "Schedule not found";
    private final ScheduleService scheduleService;
    private final LabService labService;
    private final UserService userService;
    private final ClassService classService;

    @Transactional
    public ScheduleRes createSchedule(final ScheduleReq scheduleReq) {
        var schedule = ScheduleMapper.INSTANCE.toEntity(scheduleReq);
        schedule.setLab(labService.getById(scheduleReq.getLabId())
                                  .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND)));
        schedule.setUser(userService.getById(SecurityHelper.getCurrentUserId())
                                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
        schedule.setClassEntity(classService.getById(scheduleReq.getClassId())
                                            .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND)));
        return ScheduleMapper.INSTANCE.toRes(schedule);
    }

    public Page<ScheduleRes> searchSchedule(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return scheduleService.searchSchedule(pageable).map(ScheduleMapper.INSTANCE::toRes);
    }

    public ScheduleRes getById(final Long id) {
        return ScheduleMapper.INSTANCE
            .toRes(scheduleService.getById(id)
                                  .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND)));
    }

    @Transactional
    public ScheduleRes update(final Long id, final ScheduleReq scheduleReq) {
        var scheduleFound = scheduleService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND));
        scheduleFound.setLab(labService.getById(scheduleReq.getLabId())
                                       .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND)));
        scheduleFound.setUser(userService.getById(SecurityHelper.getCurrentUserId())
                                         .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
        scheduleFound.setClassEntity(classService.getById(scheduleReq.getClassId())
                                                 .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND)));
        scheduleFound.setTimeStart(scheduleReq.getTimeStart());
        scheduleFound.setTimeUse(scheduleReq.getTimeUse());
        return ScheduleMapper.INSTANCE.toRes(scheduleService.save(scheduleFound));
    }

    @Transactional
    public ScheduleRes deleteSchedule(final Long id) {
        var scheduleFound = scheduleService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        scheduleService.delete(scheduleFound);
        return ScheduleMapper.INSTANCE.toRes(scheduleFound);
    }
}
