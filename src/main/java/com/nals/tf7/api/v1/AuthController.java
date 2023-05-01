package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.AuthBloc;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController
    extends BaseController {

    private final AuthBloc authBloc;

    public AuthController(final Validator validator, final AuthBloc authBloc) {
        super(validator);
        this.authBloc = authBloc;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginReq loginReq) {
        return ok(authBloc.authenticate(loginReq));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request, final HttpServletResponse response) {
        authBloc.logout(request, response);
        return noContent();
    }
}
