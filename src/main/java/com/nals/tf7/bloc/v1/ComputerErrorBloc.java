package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.ComputerError;
import com.nals.tf7.dto.v1.request.ComputerErrorReq;
import com.nals.tf7.dto.v1.response.ComputerErrorRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.SecurityHelper;
import com.nals.tf7.mapper.v1.ComputerErrorMapper;
import com.nals.tf7.service.v1.ComputerErrorService;
import com.nals.tf7.service.v1.ComputerService;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ComputerErrorBloc {

    private final ComputerErrorService computerErrorService;
    private final ComputerService computerService;
    private final UserService userService;

    public List<ComputerErrorRes> getAllErrorsByComputer(final Long id) {
        var computer = computerService.getById(id)
                                      .orElseThrow(() -> new NotFoundException("Computer not found"));
        return computerErrorService.getAllByClass(computer)
                                   .stream()
                                   .map(ComputerErrorMapper.INSTANCE::toRes)
                                   .collect(Collectors.toList());
    }

    @Transactional
    public Long create(final Long computerId, final ComputerErrorReq req) {
        var userId = SecurityHelper.getCurrentUserId();
        var user = userService.getById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        var computer = computerService.getById(computerId)
                                      .orElseThrow(() -> new NotFoundException("Computer not found"));

        var error = ComputerError.builder()
                                 .user(user)
                                 .computer(computer)
                                 .description(req.getDescription())
                                 .fixed(false)
                                 .build();

        computerErrorService.save(error);
        computer.setActivate(false);
        return error.getId();
    }
}
