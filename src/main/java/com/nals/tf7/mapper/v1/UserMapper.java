package com.nals.tf7.mapper.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.UserReq;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.dto.v1.response.user.ProfileRes;
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
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    ProfileRes toUserBasicInfoRes(User user);


    @Mapping(target = "role", ignore = true)
    User toEntity(UserReq req);

    User toEntity(LoginReq req);

    default Long fromInstant(Instant instant) {
        return DateHelper.toMillis(instant);
    }

    default Instant toInstant(Long millis) {
        return DateHelper.toInstant(millis);
    }
}
