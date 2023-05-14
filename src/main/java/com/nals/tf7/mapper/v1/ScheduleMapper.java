package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.Schedule;
import com.nals.tf7.dto.v1.request.ScheduleReq;
import com.nals.tf7.dto.v1.response.ScheduleRes;
import com.nals.tf7.helpers.DateHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    ScheduleRes toRes(Schedule user);

    Schedule toEntity(ScheduleReq req);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
