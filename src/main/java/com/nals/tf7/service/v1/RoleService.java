package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Role;
import com.nals.tf7.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RoleService
    extends BaseService<Role, RoleRepository> {

    public RoleService(final RoleRepository repository) {
        super(repository);
    }

    public Optional<Role> findByName(final String name) {
        return getRepository().findByName(name);
    }
}
