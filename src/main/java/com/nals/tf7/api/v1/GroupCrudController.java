package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.GroupCrudBloc;
import com.nals.tf7.dto.v1.request.GroupReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupCrudController
    extends BaseController {

    private final GroupCrudBloc groupCrudBloc;

    public GroupCrudController(final Validator validator, final GroupCrudBloc groupCrudBloc) {
        super(validator);
        this.groupCrudBloc = groupCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody final GroupReq groupReq) {
        return created(groupCrudBloc.createGroup(groupReq).getId());
    }
}
