package com.nals.tf7.dto.v1.response;

import com.nals.tf7.domain.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRes
    extends AbstractAuditingEntity {
    private Long id;
    private String avatar;
}
