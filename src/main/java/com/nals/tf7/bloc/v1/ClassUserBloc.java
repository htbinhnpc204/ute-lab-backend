package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ClassUser;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.ClassUserRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.ClassUserMapper;
import com.nals.tf7.service.v1.ClassUserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ClassUserBloc {

    public static final String CLASS_USER_NOT_FOUND = "Class user not found";
    private final ClassUserService classUserService;

    public ClassUserBloc(final ClassUserService classUserService) {
        this.classUserService = classUserService;
    }

    @Transactional
    public Long addUserToClass(final Long classId, final Long userId) {
        return classUserService.save(ClassUser.builder()
                                              .classId(classId)
                                              .userId(userId)
                                              .build()).getId();
    }

    @Transactional
    public void deleteUserFromClass(final Long classId, final Long userId) {
        var classUser = classUserService.findByClassIdAndUserId(classId, userId)
                                        .orElseThrow(() -> new NotFoundException(CLASS_USER_NOT_FOUND));
        classUserService.delete(classUser);
    }

    public Page<ClassUserRes> searchClassUsers(final Long classId, final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return classUserService.getAllByClassId(classId, pageable)
                               .map(ClassUserMapper.INSTANCE::toRes);
    }
}
