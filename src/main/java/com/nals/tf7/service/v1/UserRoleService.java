package com.nals.tf7.service.v1;

import com.nals.tf7.domain.UserRole;
import com.nals.tf7.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserRoleService
    extends BaseService<UserRole, UserRoleRepository> {

    public UserRoleService(final UserRoleRepository repository) {
        super(repository);
    }
}
