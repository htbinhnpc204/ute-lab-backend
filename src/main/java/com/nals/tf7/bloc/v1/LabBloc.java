package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.dto.v1.request.LabReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.mapper.v1.LabMapper;
import com.nals.tf7.service.v1.LabService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class LabBloc {
    public static final String LAB_NOT_FOUND = "Lab not found";
    private final LabService labService;

    public LabBloc(final LabService labService) {
        this.labService = labService;
    }

    public Lab createLab(final LabReq labReq) {
        return labService.save(LabMapper.INSTANCE.toLab(labReq));
    }

    public Page<Lab> searchLabs(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return labService.searchLabs(searchReq.getKeyword(), pageable);
    }

    public Lab getById(final Long id) {
        return labService.getById(id)
                         .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
    }

    public Lab update(final Long id, final LabReq labReq) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labFound.setName(labReq.getName());
        labFound.setAvatar(labReq.getAvatar());
        labFound.setDescription(labReq.getDescription());
        labFound.setManager(labReq.getManager());
        return labService.save(labFound);
    }

    public Lab deleteLab(final Long id) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labService.delete(labFound);
        return labFound;
    }
}
