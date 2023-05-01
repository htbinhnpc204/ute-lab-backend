package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.UserCrudBloc;
import com.nals.tf7.dto.v1.request.auth.RegisterReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/users")
public class UserCrudController
    extends BaseController {

    private final UserCrudBloc userCrudBloc;

    public UserCrudController(final Validator validator, final UserCrudBloc userCrudBloc) {
        super(validator);
        this.userCrudBloc = userCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody final RegisterReq req) {
        return created(1L);
    }
}
