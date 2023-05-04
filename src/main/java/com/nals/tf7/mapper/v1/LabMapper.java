package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.dto.v1.request.LabReq;
import com.nals.tf7.dto.v1.response.LabRes;
import com.nals.tf7.helpers.DateHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LabMapper {

    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    Lab toEntity(LabReq req);

    LabRes toLabRes(Lab lab);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
