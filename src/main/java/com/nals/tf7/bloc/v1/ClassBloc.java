package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.dto.v1.request.ClassReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.ClassRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.ClassMapper;
import com.nals.tf7.service.v1.ClassService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClassBloc {

    public static final String CLASS_NOT_FOUND = "Class not found";
    private final ClassService classService;

    public ClassBloc(final ClassService classService) {
        this.classService = classService;
    }

    @Transactional
    public ClassEntity createClass(final ClassReq labReq) {
        var aClass = ClassMapper.INSTANCE.toEntity(labReq);
        return classService.save(aClass);
    }

    public Page<ClassRes> searchClasses(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return classService.searchClasses(searchReq.getKeyword(), pageable)
                           .map(ClassMapper.INSTANCE::toRes);
    }

    public ClassRes getById(final Long id) {
        var result = classService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));

        return ClassMapper.INSTANCE.toRes(result);
    }

    @Transactional
    public ClassRes update(final Long id, final ClassReq labReq) {
        var classFound = classService.getById(id)
                                     .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        classFound.setName(labReq.getName());
        return ClassMapper.INSTANCE.toRes(classService.save(classFound));
    }

    @Transactional
    public Long deleteClass(final Long id) {
        var classFound = classService.getById(id)
                                   .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        classService.delete(classFound);
        return classFound.getId();
    }
}
