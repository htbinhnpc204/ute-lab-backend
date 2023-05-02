package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.dto.v1.request.ClassReq;
import com.nals.tf7.dto.v1.request.SearchReq;
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

    public Page<ClassEntity> searchClasses(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return classService.searchClasses(searchReq.getKeyword(), pageable);
    }

    public ClassEntity getById(final Long id) {
        return classService.getById(id)
                           .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
    }

    @Transactional
    public ClassEntity update(final Long id, final ClassReq labReq) {
        var classFound = classService.getById(id)
                                     .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        classFound.setName(labReq.getName());
        return classService.save(classFound);
    }

    @Transactional
    public ClassEntity deleteClass(final Long id) {
        var labFound = classService.getById(id)
                                   .orElseThrow(() -> new NotFoundException(CLASS_NOT_FOUND));
        classService.delete(labFound);
        return labFound;
    }
}
