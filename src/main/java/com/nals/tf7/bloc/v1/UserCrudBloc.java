package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.request.UserReq;
import com.nals.tf7.dto.v1.response.user.ProfileRes;
import com.nals.tf7.errors.FileException;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.mapper.v1.UserMapper;
import com.nals.tf7.service.v1.FileService;
import com.nals.tf7.service.v1.MinioService;
import com.nals.tf7.service.v1.RoleService;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.nals.tf7.config.ErrorConstants.EMAIL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.nals.tf7.config.ErrorConstants.ROLE_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.USER_NOT_FOUND;
import static com.nals.tf7.errors.ErrorCodes.INVALID_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCrudBloc {
    public static final String STUDENT_ID_ALREADY_EXISTS = "Student ID already exists";
    public static final String STUDENT_ID = "student_id";
    private final UserService userService;
    private final RoleService roleService;
    private final MinioService minioService;
    private final FileService fileService;
    private final PasswordEncoder encoder;

    public ProfileRes getProfile() {
        Long currentUserId = SecurityHelper.getCurrentUserId();
        log.info("Get basic info of user has currentUserId #{}", currentUserId);

        return UserMapper.INSTANCE.toUserBasicInfoRes(userService.getBasicInfoById(currentUserId));
    }

    @Transactional
    public Long create(final UserReq req) {
        validateUserReq(req, "create");
        User user = UserMapper.INSTANCE.toEntity(req);
        if (StringHelper.isNotBlank(handleFileUpload(req))) {
            user.setAvatar(handleFileUpload(req));
        }
        user.setLangKey("EN");
        user.setActivated(true);
        user.setRole(roleService.findByName(req.getRole())
                                .orElseThrow(() -> new NotFoundException(ROLE_NOT_FOUND)));
        user.setPassword(encoder.encode("123"));
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
        log.info("user: #{}", user);
        return UserMapper.INSTANCE.toUserBasicInfoRes(user);
    }

    public Page<ProfileRes> searchUsers(final SearchReq req) {
        var pageable = PaginationHelper.generatePageRequest(req);
        return userService.searchUsers(req.getKeyword(), pageable).map(UserMapper.INSTANCE::toUserBasicInfoRes);
    }

    @Transactional
    public Long update(final Long id, final UserReq req) {
        validateUserReq(req, "update");

        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(req.getEmail());
        user.setStudentId(req.getStudentId());
        user.setName(req.getName());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setGender(req.getGender());
        user.setDob(req.getDob());
        if (StringHelper.isNotBlank(handleFileUpload(req))) {
            user.setAvatar(handleFileUpload(req));
        }
        return userService.save(user).getId();
    }

    @Transactional
    public void delete(final Long id) {
        var user = userService.getOneById(id)
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        userService.delete(user);
    }

    private String handleFileUpload(final UserReq userReq) {
        String fileName = StringHelper.EMPTY;
        if (userReq.getAvatar() != null && !userReq.getAvatar().isEmpty()) {
            var avatar = userReq.getAvatar();
            fileName = fileService.generateFileName(avatar.getOriginalFilename());
            try {
                minioService.uploadFile(fileName, avatar.getInputStream(), avatar.getSize());
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        }
        return fileName;
    }

    private void validateUserReq(final UserReq req, final String type) {
        if (userService.existsByEmail(req.getEmail()) && type.equals("create")) {
            throw new ValidatorException(EMAIL_ALREADY_EXISTS, EMAIL, INVALID_DATA);
        }

        if (userService.existsByStudentId(req.getStudentId()) && type.equals("create")) {
            throw new ValidatorException(STUDENT_ID_ALREADY_EXISTS, STUDENT_ID, INVALID_DATA);
        }
    }
}
