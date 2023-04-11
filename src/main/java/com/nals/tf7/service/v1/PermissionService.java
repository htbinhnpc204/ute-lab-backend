package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Permission;
import com.nals.tf7.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PermissionService
    extends BaseService<Permission, PermissionRepository> {

    public PermissionService(final PermissionRepository repository) {
        super(repository);
    }
}
