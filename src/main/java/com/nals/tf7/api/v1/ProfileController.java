package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.UserCrudBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/me")
public class ProfileController
    extends BaseController {

    private final UserCrudBloc userCrudBloc;

    public ProfileController(final Validator validator, final UserCrudBloc userCrudBloc) {
        super(validator);
        this.userCrudBloc = userCrudBloc;
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {
        return ok(userCrudBloc.getProfile());
    }
}
