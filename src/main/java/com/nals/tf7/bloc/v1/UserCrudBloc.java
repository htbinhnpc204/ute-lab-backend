package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.UserReq;
import com.nals.tf7.dto.v1.response.user.ProfileRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.mapper.v1.UserMapper;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nals.tf7.config.ErrorConstants.EMAIL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.nals.tf7.config.ErrorConstants.USER_NOT_FOUND;
import static com.nals.tf7.errors.ErrorCodes.INVALID_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCrudBloc {
    private final UserService userService;
    private final PasswordEncoder encoder;

    public ProfileRes getProfile() {
        Long currentUserId = SecurityHelper.getCurrentUserId();
        log.info("Get basic info of user has currentUserId #{}", currentUserId);

        return UserMapper.INSTANCE.toUserBasicInfoRes(userService.getBasicInfoById(currentUserId));
    }

    @Transactional
    public Long create(final UserReq req) {
        User user = UserMapper.INSTANCE.toEntity(req);
        user.setPassword(encoder.encode("password"));
        return userService.save(user).getId();
    }

    @Transactional
    public void updateActivated(final Long id) {
        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setActivated(!user.isActivated());
        userService.save(user);
    }

    public ProfileRes getOneById(final Long id) {
        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return UserMapper.INSTANCE.toUserBasicInfoRes(user);
    }

    public List<ProfileRes> fetchAll() {
        return userService.fetchAll().stream().map(UserMapper.INSTANCE::toUserBasicInfoRes)
                          .collect(Collectors.toList());
    }

    @Transactional
    public Long update(final Long id, final UserReq req) {
        validateUserReq(req);

        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(req.getEmail());
        user.setStudentId(req.getStudentId());
        user.setName(req.getName());
        user.setAvatar(req.getAvatar());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setGender(req.getGender());
        user.setDob(req.getDob());
        return userService.save(user).getId();
    }

    public void delete(final Long id) {
        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        userService.delete(user);
    }

    private void validateUserReq(final UserReq req) {
        if (userService.existsByEmail(req.getEmail())) {
            throw new ValidatorException(EMAIL_ALREADY_EXISTS, EMAIL, INVALID_DATA);
        }
    }
}
