package com.nals.tf7.service.v1;

import com.nals.tf7.domain.Computer;
import com.nals.tf7.domain.ComputerError;
import com.nals.tf7.repository.ComputerErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ComputerErrorService
    extends BaseService<ComputerError, ComputerErrorRepository> {

    public ComputerErrorService(final ComputerErrorRepository repository) {
        super(repository);
    }

    public List<ComputerError> getAllByClass(final Computer computer) {
        log.info("Get all error for #{}", computer);
        return getRepository().getAllByComputer(computer);
    }
}
