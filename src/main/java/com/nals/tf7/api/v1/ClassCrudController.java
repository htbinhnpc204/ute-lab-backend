package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ClassCrudBloc;
import com.nals.tf7.dto.v1.request.GroupReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/groups")
public class ClassCrudController
    extends BaseController {

    private final ClassCrudBloc classCrudBloc;

    public ClassCrudController(final Validator validator, final ClassCrudBloc classCrudBloc) {
        super(validator);
        this.classCrudBloc = classCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody final GroupReq groupReq) {
        return created(classCrudBloc.createGroup(groupReq).getId());
    }
}
