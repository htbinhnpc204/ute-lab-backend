package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Computer;
import com.nals.tf7.dto.v1.request.ComputerReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.ComputerRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.ComputerMapper;
import com.nals.tf7.service.v1.ComputerService;
import com.nals.tf7.service.v1.LabService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nals.tf7.config.ErrorConstants.COMPUTER_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.LAB_NOT_FOUND;

@Transactional(readOnly = true)
@Service
public class ComputerBloc {

    private final ComputerService computerService;
    private final LabService labService;

    public ComputerBloc(final ComputerService computerService, final LabService labService) {
        this.computerService = computerService;
        this.labService = labService;
    }

    @Transactional
    public Computer createComputer(final ComputerReq req) {
        var computer = ComputerMapper.INSTANCE.toEntity(req);
        computer.setLab(labService.getById(req.getLabId())
                                  .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND)));
        return computerService.save(computer);
    }

    public Page<ComputerRes> searchComputers(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return computerService.searchComputers(searchReq.getKeyword(), pageable)
                              .map(ComputerMapper.INSTANCE::toComputerRes);
    }

    public Computer getById(final Long id) {
        return computerService.getById(id)
                              .orElseThrow(() -> new NotFoundException(COMPUTER_NOT_FOUND));
    }

    @Transactional
    public Computer update(final Long id, final ComputerReq req) {
        var computerFound = computerService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(COMPUTER_NOT_FOUND));
        computerFound.setName(req.getName());
        computerFound.setDescription(req.getDescription());
        computerFound.setLab(labService.getById(req.getLabId())
                                       .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND)));
        return computerService.save(computerFound);
    }

    @Transactional
    public Computer updateStatus(final Long id) {
        var computerFound = computerService.getById(id)
                                           .orElseThrow(() -> new NotFoundException(COMPUTER_NOT_FOUND));
        computerFound.setActivate(!computerFound.isActivate());
        return computerService.save(computerFound);
    }

    @Transactional
    public Computer deleteLab(final Long id) {
        var computer = computerService.getById(id)
                                      .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        computerService.delete(computer);
        return computer;
    }
}
