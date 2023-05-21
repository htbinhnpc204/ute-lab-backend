package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ClassUser;
import com.nals.tf7.dto.v1.request.ClassUserReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.ClassUserRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.ClassUserMapper;
import com.nals.tf7.service.v1.ClassService;
import com.nals.tf7.service.v1.ClassUserService;
import com.nals.tf7.service.v1.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ClassUserBloc {

    public static final String CLASS_USER_NOT_FOUND = "Class user not found";
    private final ClassUserService classUserService;
    private final UserService userService;
    private final ClassService classService;

    public ClassUserBloc(final ClassUserService classUserService, final UserService userService,
                         final ClassService classService) {
        this.classUserService = classUserService;
        this.userService = userService;
        this.classService = classService;
    }

    @Transactional
    public Long addUserToClass(final Long classId, final ClassUserReq req) {
        var clsUser = ClassUser.builder();
        userService.getById(req.getUserId()).ifPresent(clsUser::user);
        classService.getById(classId).ifPresent(clsUser::classEntity);
        clsUser.role(req.getRole());

        return classUserService.save(clsUser.build()).getId();
    }

    @Transactional
    public void deleteUserFromClass(final Long classId, final Long userId) {
        var classEntity = classService.getById(classId).orElse(null);
        var user = userService.getById(userId).orElse(null);
        var classUser = classUserService.findByClassAndUser(classEntity, user)
                                        .orElseThrow(() -> new NotFoundException(CLASS_USER_NOT_FOUND));
        classUserService.delete(classUser);
    }

    public Page<ClassUserRes> searchClassUsers(final Long classId, final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        var classEntity = classService.getById(classId).orElse(null);
        return classUserService.getAllByClass(classEntity, pageable)
                               .map(ClassUserMapper.INSTANCE::toRes);
    }
}
