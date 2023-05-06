package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.ClassUser;
import com.nals.tf7.dto.v1.response.ClassUserRes;
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
public interface ClassUserMapper {

    ClassUserMapper INSTANCE = Mappers.getMapper(ClassUserMapper.class);

    ClassUserRes toRes(ClassUser classUser);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
