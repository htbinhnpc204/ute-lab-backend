package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.AuthBloc;
import com.nals.tf7.dto.v1.request.auth.ForgotPasswordReq;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.dto.v1.request.auth.RegisterReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public ResponseEntity<?> login(@Valid @RequestBody final LoginReq loginReq) {
        return ok(authBloc.authenticate(loginReq));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody final RegisterReq registerReq) {
        return ok(authBloc.register(registerReq));
    }

    @PostMapping("/password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody final ForgotPasswordReq req) {

        return ok(authBloc.forgotPassword(req));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request, final HttpServletResponse response) {
        authBloc.logout(request, response);
        return noContent();
    }
}
