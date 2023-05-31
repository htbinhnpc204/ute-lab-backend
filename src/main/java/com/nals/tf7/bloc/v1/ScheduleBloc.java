package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.domain.Lab;
import com.nals.tf7.dto.v1.request.ScheduleReq;
import com.nals.tf7.dto.v1.request.ScheduleSearchReq;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (Objects.nonNull(scheduleReq.getClassId())) {
            schedule.setClassEntity(classService.getById(scheduleReq.getClassId())
                                                .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND)));
        }

        schedule.setApproved(false);
        return ScheduleMapper.INSTANCE.toRes(scheduleService.save(schedule));
    }

    public Page<ScheduleRes> searchSchedule(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return scheduleService.searchSchedule(pageable).map(ScheduleMapper.INSTANCE::toRes);
    }

    public List<ScheduleRes> searchByLab(final Long labId) {
        var lab = labService.getById(labId)
                            .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        return scheduleService.searchByLab(lab)
                              .stream()
                              .map(ScheduleMapper.INSTANCE::toRes)
                              .collect(Collectors.toList());
    }

    public List<ScheduleRes> searchByClass(final Long classId) {
        var classEntity = classService.getById(classId)
                                      .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        return scheduleService.searchByClass(classEntity)
                              .stream()
                              .map(ScheduleMapper.INSTANCE::toRes)
                              .collect(Collectors.toList());
    }

    public List<ScheduleRes> searchByLabAndClass(final ScheduleSearchReq req) {
        Lab lab = null;
        ClassEntity classEntity = null;
        if (Objects.nonNull(req)) {
            if (Objects.nonNull(req.getLabId())) {
                lab = labService.getById(req.getLabId())
                                .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
            }
            if (Objects.nonNull(req.getClassId())) {
                classEntity = classService.getById(req.getClassId())
                                          .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
            }
        }

        var pageReq = PaginationHelper.generatePageRequest(req);

        return scheduleService.searchByLabAndClass(lab, classEntity, pageReq)
            .stream()
                              .map(ScheduleMapper.INSTANCE::toRes)
            .collect(Collectors.toList());
    }

    public ScheduleRes getById(final Long id) {
        return ScheduleMapper.INSTANCE
            .toRes(scheduleService.getById(id)
                                  .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND)));
    }

    public void approveSchedule(final Long id) {
        var scheduleFound = scheduleService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND));
        scheduleFound.setApproved(true);
        scheduleService.save(scheduleFound);
    }

    @Transactional
    public ScheduleRes update(final Long id, final ScheduleReq scheduleReq) {
        var scheduleFound = scheduleService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(SCHEDULE_NOT_FOUND));
        scheduleFound.setLab(labService.getById(scheduleReq.getLabId())
                                       .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND)));
        scheduleFound.setUser(userService.getById(SecurityHelper.getCurrentUserId())
                                         .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
        if (Objects.nonNull(scheduleReq.getClassId())) {
            scheduleFound.setClassEntity(classService.getById(scheduleReq.getClassId())
                                                .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND)));
        }

        scheduleFound.setTimeStart(scheduleReq.getTimeStart());
        scheduleFound.setTimeUse(scheduleReq.getTimeUse());
        return ScheduleMapper.INSTANCE.toRes(scheduleService.save(scheduleFound));
    }

    @Transactional
    public void deleteSchedule(final Long id) {
        var scheduleFound = scheduleService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        scheduleService.delete(scheduleFound);
    }
}
