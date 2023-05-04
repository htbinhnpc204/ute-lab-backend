package com.nals.tf7.service.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService
    extends BaseService<User, UserRepository> {

    public UserService(final UserRepository repository) {
        super(repository);
    }

    public boolean existsByEmail(final String email) {
        return getRepository().existsByEmail(email);
    }

    public User getBasicInfoById(final Long id) {
        log.info("Get basic info by id #{}", id);
        return getRepository().getBasicInfoById(id);
    }

    public User create(final User user) {
        return getRepository().save(user);
    }

    public Optional<User> getOneById(final Long id) {
        return getRepository().findById(id);
    }
}
