package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.repository.LabRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class LabService
    extends BaseService<Lab, LabRepository> {

    public LabService(final LabRepository repository) {
        super(repository);
    }

    public Page<Lab> searchLabs(final String name, final PageRequest req) {
        return getRepository().findByNameContainingIgnoreCase(name, req);
    }
}
