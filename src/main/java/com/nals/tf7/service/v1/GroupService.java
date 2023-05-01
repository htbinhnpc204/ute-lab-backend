package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GroupService
    extends BaseService<Lab, GroupRepository> {

    public GroupService(final GroupRepository repository) {
        super(repository);
    }

//    public Page<Class> searchGroups(final PageRequest pageRequest, final String name) {
//        log.info("Search groups by name #{} with data #{}", name, pageRequest);
//        return getRepository().searchByName(pageRequest, name);
//    }
//
//    public Page<Class> searchByUserIdAndType(final Long userId, final GroupUserType type, final Pageable pageable) {
//        log.info("Search groups by user id #{} and type #{} with data #{}", userId, type, pageable);
//        return getRepository().searchByUserIdAndType(userId, type, pageable);
//    }
//
//    public boolean existsByName(final String name) {
//        log.info("Check group name is exists: #{}", name);
//        return getRepository().existsByName(name);
//    }
}
