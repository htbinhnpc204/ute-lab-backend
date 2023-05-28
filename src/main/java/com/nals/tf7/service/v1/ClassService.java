package com.nals.tf7.service.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.repository.ClassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ClassService
    extends BaseService<ClassEntity, ClassRepository> {

    public ClassService(final ClassRepository repository) {
        super(repository);
    }

    public boolean existsByName(final String name) {
        return getRepository().existsByName(name);
    }

    public Page<ClassEntity> searchClasses(final String name, final PageRequest req) {
        return getRepository().findByNameContainingIgnoreCase(name, req);
    }
}
