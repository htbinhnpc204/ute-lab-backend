package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Lab;
import com.nals.tf7.dto.v1.request.GroupReq;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.service.v1.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.nals.tf7.config.ErrorConstants.GROUP_NAME_NOT_NULL;
import static com.nals.tf7.config.ErrorConstants.GROUP_TYPE;
import static com.nals.tf7.config.ErrorConstants.GROUP_TYPE_NOT_NULL;
import static com.nals.tf7.config.ErrorConstants.NAME;
import static com.nals.tf7.errors.ErrorCodes.INVALID_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassCrudBloc {

    private final GroupService groupService;

    @Transactional
    public Lab createGroup(final GroupReq groupReq) {
        log.info("Create group with: #{}", groupReq);

        validGroupReq(groupReq);

        return groupService.save(Lab.builder()
                                    .name(groupReq.getName())
                                    .description(groupReq.getDescription())
                                    .avatar(groupReq.getImageUrl())
                                    .build());
    }

    private void validGroupReq(final GroupReq groupReq) {
        if (StringHelper.isBlank(groupReq.getName())) {
            throw new ValidatorException(NAME, GROUP_NAME_NOT_NULL, INVALID_DATA);
        }

        if (Objects.isNull(groupReq.getType())) {
            throw new ValidatorException(GROUP_TYPE, GROUP_TYPE_NOT_NULL, INVALID_DATA);
        }
    }
}
