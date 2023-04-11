package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.AuthBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<?> login() {
        return noContent();
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return noContent();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request, final HttpServletResponse response) {
        return noContent();
    }
}
