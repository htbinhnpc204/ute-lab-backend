package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Group;
import com.nals.tf7.dto.v1.response.GroupSearchRes;
import com.nals.tf7.enums.GroupUserType;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.mapper.v1.GroupMapper;
import com.nals.tf7.service.v1.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupListBloc {

    private final GroupService groupService;

    public Page<GroupSearchRes> searchGroups(final PageRequest pageRequest, final String name) {
        Page<Group> groups;
        if (SecurityHelper.isAdminOrBOD()) {
            groups = groupService.searchGroups(pageRequest, name);
        } else {
            var userId = SecurityHelper.getCurrentUserId();
            groups = groupService.searchByUserIdAndType(userId, GroupUserType.GROUP, pageRequest);
        }

        return groups
            .map(GroupMapper.INSTANCE::toGroupRes);
    }
}
