package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Computer;
import com.nals.tf7.repository.ComputerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ComputerService
    extends BaseService<Computer, ComputerRepository> {

    public ComputerService(final ComputerRepository repository) {
        super(repository);
    }

    public Page<Computer> searchComputers(final String name, final PageRequest req) {
        return getRepository().findByNameContainingIgnoreCase(name, req);
    }
}
