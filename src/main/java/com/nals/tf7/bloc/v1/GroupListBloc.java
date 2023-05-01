package com.nals.tf7.bloc.v1;

import com.nals.tf7.service.v1.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupListBloc {

    private final GroupService groupService;

//    public Page<GroupSearchRes> searchGroups(final PageRequest pageRequest, final String name) {
//        Page<Class> groups;
//        if (SecurityHelper.isAdminOrBOD()) {
//            groups = groupService.searchGroups(pageRequest, name);
//        } else {
//            var userId = SecurityHelper.getCurrentUserId();
//            groups = groupService.searchByUserIdAndType(userId, GroupUserType.GROUP, pageRequest);
//        }
//
//        return groups
//            .map(ClassMapper.INSTANCE::toGroupRes);
//    }
}
