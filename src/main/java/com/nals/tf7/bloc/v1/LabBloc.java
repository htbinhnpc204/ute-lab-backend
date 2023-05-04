package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.dto.v1.request.LabReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.LabRes;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.LabMapper;
import com.nals.tf7.service.v1.LabService;
import com.nals.tf7.service.v1.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nals.tf7.config.ErrorConstants.LAB_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
public class LabBloc {
    private final LabService labService;
    private final UserService userService;

    public LabBloc(final LabService labService, final UserService userService) {
        this.labService = labService;
        this.userService = userService;
    }

    @Transactional
    public Lab createLab(final LabReq labReq) {
        var lab = LabMapper.INSTANCE.toEntity(labReq);
        lab.setManager(userService.getById(labReq.getManager())
                                  .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
        return labService.save(lab);
    }

    public Page<LabRes> searchLabs(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return labService.searchLabs(searchReq.getKeyword(), pageable).map(LabMapper.INSTANCE::toLabRes);
    }

    public Lab getById(final Long id) {
        return labService.getById(id)
                         .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
    }

    @Transactional
    public Lab update(final Long id, final LabReq labReq) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labFound.setName(labReq.getName());
        labFound.setAvatar(labReq.getAvatar());
        labFound.setDescription(labReq.getDescription());
        labFound.setManager(userService.getById(labReq.getManager())
                                       .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
        return labService.save(labFound);
    }

    @Transactional
    public Lab deleteLab(final Long id) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labService.delete(labFound);
        return labFound;
    }
}
