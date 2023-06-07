package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.ComputerError;
import com.nals.tf7.dto.v1.response.ComputerErrorRes;
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
public interface ComputerErrorMapper {

    ComputerErrorMapper INSTANCE = Mappers.getMapper(ComputerErrorMapper.class);

    ComputerErrorRes toRes(ComputerError computerError);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
