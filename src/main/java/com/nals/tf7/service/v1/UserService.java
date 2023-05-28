package com.nals.tf7.service.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public boolean existsByStudentId(final String studentId) {
        return getRepository().existsByStudentId(studentId);
    }

    public User getBasicInfoById(final Long id) {
        log.info("Get basic info by id #{}", id);
        return getRepository().getBasicInfoById(id);
    }

    public Page<User> searchUsers(final String name, final PageRequest req) {
        return getRepository().findByNameContainingIgnoreCase(name, req);
    }

    public User create(final User user) {
        return getRepository().save(user);
    }

    public Optional<User> getOneById(final Long id) {
        return getRepository().findById(id);
    }

    public Optional<User> getOneByStudentId(final String studentId) {
        log.info("Get user by student id #{}", studentId);
        return getRepository().findOneByStudentIdAndActivatedIsTrue(studentId);
    }
}
