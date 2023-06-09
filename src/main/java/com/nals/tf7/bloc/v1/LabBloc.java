package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.LabReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.dto.v1.response.LabRes;
import com.nals.tf7.errors.FileException;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.PaginationHelper;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.mapper.v1.LabMapper;
import com.nals.tf7.service.v1.FileService;
import com.nals.tf7.service.v1.LabService;
import com.nals.tf7.service.v1.MinioService;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.nals.tf7.config.ErrorConstants.LAB_NOT_FOUND;
import static com.nals.tf7.config.ErrorConstants.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LabBloc {
    private final LabService labService;
    private final UserService userService;
    private final MinioService minioService;
    private final FileService fileService;

    @Transactional
    public Lab createLab(final LabReq labReq) {
        var lab = LabMapper.INSTANCE.toEntity(labReq);
        if (StringHelper.isNotBlank(handleFileUpload(labReq))) {
            lab.setAvatar(handleFileUpload(labReq));
        }

        var user = userService.getById(labReq.getManager())
                              .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        validateUser(user);

        lab.setManager(user);
        return labService.save(lab);
    }

    public Page<LabRes> searchLabs(final SearchReq searchReq) {
        var pageable = PaginationHelper.generatePageRequest(searchReq);
        return labService.searchLabs(searchReq.getKeyword(), pageable).map(LabMapper.INSTANCE::toLabRes);
    }

    public LabRes getById(final Long id) {
        var lab = labService.getById(id)
                            .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        return LabMapper.INSTANCE.toLabRes(lab);
    }

    @Transactional
    public LabRes update(final Long id, final LabReq labReq) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labFound.setName(labReq.getName());
        if (StringHelper.isNotBlank(handleFileUpload(labReq))) {
            labFound.setAvatar(handleFileUpload(labReq));
        }

        labFound.setDescription(labReq.getDescription());
        var user = userService.getById(labReq.getManager())
                   .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        validateUser(user);
        labFound.setManager(user);
        return LabMapper.INSTANCE.toLabRes(labService.save(labFound));
    }

    private String handleFileUpload(final LabReq labReq) {
        String fileName = StringHelper.EMPTY;
        if (labReq.getAvatar() != null && !labReq.getAvatar().isEmpty()) {
            var avatar = labReq.getAvatar();
            fileName = fileService.generateFileName(avatar.getOriginalFilename());
            try {
                minioService.uploadFile(fileName, avatar.getInputStream(), avatar.getSize());
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        }
        return fileName;
    }

    private void validateUser(final User user) {
        if (!user.getRole().getName().equalsIgnoreCase("ROLE_QUAN_TRI")
            && !user.getRole().getName().equalsIgnoreCase("ROLE_GIAO_VIEN")) {
            throw new ValidatorException("User doesn't have right role");
        }
    }

    @Transactional
    public Lab deleteLab(final Long id) {
        var labFound = labService.getById(id)
                                 .orElseThrow(() -> new NotFoundException(LAB_NOT_FOUND));
        labService.delete(labFound);
        return labFound;
    }
}
