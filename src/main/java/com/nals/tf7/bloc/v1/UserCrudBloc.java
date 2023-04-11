package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.response.user.ProfileRes;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.mapper.v1.UserMapper;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCrudBloc {
    private final UserService userService;

    public ProfileRes getProfile() {
        Long currentUserId = SecurityHelper.getCurrentUserId();
        log.info("Get basic info of user has currentUserId #{}", currentUserId);

        User user = userService.getBasicInfoById(currentUserId);
        ProfileRes response = UserMapper.INSTANCE.toUserBasicInfoRes(user);
//        response.setImageUrl(fileService.getFullFileUrl(user.getImageName()));

        return response;
    }
}
