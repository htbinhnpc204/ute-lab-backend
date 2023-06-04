package com.nals.tf7.dto.v1.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nals.tf7.dto.v1.response.user.ProfileRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleRes {
    private Long id;
    private LabRes lab;
    private ClassRes classRes;
    private ProfileRes register;
    private Instant timeStart;
    private Integer timeUse;
    private boolean approved;
}
