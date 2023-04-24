package com.nals.tf7.dto.v1.response;

import com.nals.tf7.domain.AbstractAuditingEntity;
import com.nals.tf7.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupSearchRes
    extends AbstractAuditingEntity {
    private Long id;
    private String name;
    private String description;
    private GroupType type;
    private String avatar;
    private List<UserInfoRes> users;
}
