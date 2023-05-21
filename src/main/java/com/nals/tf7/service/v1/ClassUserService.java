package com.nals.tf7.service.v1;

import com.nals.tf7.domain.ClassEntity;
import com.nals.tf7.domain.ClassUser;
import com.nals.tf7.domain.User;
import com.nals.tf7.repository.ClassUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ClassUserService
    extends BaseService<ClassUser, ClassUserRepository> {
    public ClassUserService(final ClassUserRepository repository) {
        super(repository);
    }

    public Page<ClassUser> getAllByClass(final ClassEntity classEntity, final PageRequest req) {
        log.info("Fetch all user by class id #{} and data #{}", classEntity, req);
        return getRepository().getAllByClassEntityOrderByRoleAsc(classEntity, req);
    }

    public Optional<ClassUser> findByClassAndUser(final ClassEntity classEntity, final User user) {
        log.info("Find class user by class id #{} and user id #{}", classEntity, user);
        return getRepository().findByClassEntityAndUser(classEntity, user);
    }
}
