package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.dto.v1.request.auth.RegisterReq;
import com.nals.tf7.dto.v1.response.OAuthTokenRes;
import com.nals.tf7.enums.Gender;
import com.nals.tf7.errors.InvalidCredentialException;
import com.nals.tf7.errors.NotFoundException;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.helpers.ValidatorHelper;
import com.nals.tf7.security.DomainUserDetails;
import com.nals.tf7.security.jwt.TokenProvider;
import com.nals.tf7.service.v1.RedisService;
import com.nals.tf7.service.v1.RoleService;
import com.nals.tf7.service.v1.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

import static com.nals.tf7.config.Constants.COMMA;
import static com.nals.tf7.config.ErrorConstants.EMAIL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_NOT_NULL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_PATTERN_INVALID;
import static com.nals.tf7.config.ErrorConstants.PASSWORD;
import static com.nals.tf7.config.ErrorConstants.PASSWORD_NOT_NULL;
import static com.nals.tf7.config.ErrorConstants.ROLE_NOT_FOUND;
import static com.nals.tf7.errors.ErrorCodes.INVALID_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthBloc {

    private static final int MINUTE_IN_SECOND = 3600;
    private static final int HOUR_IN_SECOND = MINUTE_IN_SECOND * 60;
    public static final String ROLE_SINH_VIEN = "ROLE_SINH_VIEN";

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public OAuthTokenRes authenticate(final LoginReq loginReq) {
        validateLoginReq(loginReq);

        var usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());

        Authentication authentication = authenticationManagerBuilder.
            getObject().authenticate(usernamePasswordAuthenticationToken);

        var domainUserDetails = (DomainUserDetails) authentication.getPrincipal();

        String roles = domainUserDetails.getRoles().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(COMMA));

        String permissions = domainUserDetails.getAuthorities().stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .collect(Collectors.joining(COMMA));

        var oAuthToken = tokenProvider.createOAuthToken(domainUserDetails.getId(),
                                                        authentication.getName(),
                                                        roles,
                                                        permissions);

        return OAuthTokenRes.builder()
                            .accessToken(oAuthToken.getAccessToken().getValue())
                            .refreshToken(oAuthToken.getRefreshToken().getValue())
                            .build();
    }

    @Transactional
    public OAuthTokenRes register(final RegisterReq req) {
        validateRegisterReq(req);
        User user = User.builder()
                        .name(req.getEmail().split("@")[0])
                        .email(req.getEmail())
                        .password(encoder.encode(req.getPassword()))
                        .gender(Gender.get(req.getGender()))
                        .langKey("EN")
                        .activated(true)
                        .role(roleService.findByName(ROLE_SINH_VIEN)
                                         .orElseThrow(() -> new NotFoundException(ROLE_NOT_FOUND)))
                        .build();
        user = userService.save(user);

        if (user.getId() != null && user.isActivated()) {
            return authenticate(LoginReq.builder()
                                        .email(req.getEmail())
                                        .password(req.getPassword())
                                        .build());
        }

        return null;
    }

    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        String token = tokenProvider.resolveToken(request);
        if (token != null) {
            redisService.setValue(token, token, HOUR_IN_SECOND);
        }

        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    private void validateLoginReq(final LoginReq loginReq) {
        if (StringHelper.isBlank(loginReq.getEmail())) {
            throw new InvalidCredentialException(EMAIL, EMAIL_NOT_NULL, INVALID_DATA);
        }

        if (StringHelper.isBlank(loginReq.getPassword())) {
            throw new InvalidCredentialException(PASSWORD, PASSWORD_NOT_NULL, INVALID_DATA);
        }

        if (!ValidatorHelper.isValidEmail(loginReq.getEmail())) {
            throw new InvalidCredentialException(EMAIL, EMAIL_PATTERN_INVALID, INVALID_DATA);
        }
    }

    private void validateRegisterReq(final RegisterReq req) {
        if (userService.existsByEmail(req.getEmail())) {
            throw new ValidatorException("Email already exists", EMAIL, INVALID_DATA);
        }
    }
}
