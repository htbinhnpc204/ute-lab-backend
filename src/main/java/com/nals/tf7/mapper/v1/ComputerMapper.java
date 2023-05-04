package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.Computer;
import com.nals.tf7.dto.v1.request.ComputerReq;
import com.nals.tf7.dto.v1.response.ComputerRes;
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
public interface ComputerMapper {
    ComputerMapper INSTANCE = Mappers.getMapper(ComputerMapper.class);

    @Mapping(target = "lab", ignore = true)
    Computer toEntity(ComputerReq req);

    ComputerRes toComputerRes(Computer computer);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
