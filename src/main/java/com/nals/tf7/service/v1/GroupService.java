package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Group;
import com.nals.tf7.enums.GroupUserType;
import com.nals.tf7.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GroupService
    extends BaseService<Group, GroupRepository> {

    public GroupService(final GroupRepository repository) {
        super(repository);
    }

    public Page<Group> searchGroups(final PageRequest pageRequest, final String name) {
        log.info("Search groups by name #{} with data #{}", name, pageRequest);
        return getRepository().searchByName(pageRequest, name);
    }

    public Page<Group> searchByUserIdAndType(final Long userId, final GroupUserType type, final Pageable pageable) {
        log.info("Search groups by user id #{} and type #{} with data #{}", userId, type, pageable);
        return getRepository().searchByUserIdAndType(userId, type, pageable);
    }
}
