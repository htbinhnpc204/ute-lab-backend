package com.nals.tf7.bloc.v1;

import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.dto.v1.response.OAuthTokenRes;
import com.nals.tf7.errors.InvalidCredentialException;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.helpers.ValidatorHelper;
import com.nals.tf7.security.DomainUserDetails;
import com.nals.tf7.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.nals.tf7.config.Constants.COMMA;
import static com.nals.tf7.config.ErrorConstants.EMAIL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_NOT_NULL;
import static com.nals.tf7.config.ErrorConstants.EMAIL_PATTERN_INVALID;
import static com.nals.tf7.config.ErrorConstants.PASSWORD;
import static com.nals.tf7.config.ErrorConstants.PASSWORD_NOT_NULL;
import static com.nals.tf7.errors.ErrorCodes.INVALID_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthBloc {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

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
                                                        String.join(",", roles),
                                                        String.join(",", permissions));

        return OAuthTokenRes.builder()
                            .accessToken(oAuthToken.getAccessToken().getValue())
                            .refreshToken(oAuthToken.getRefreshToken().getValue())
                            .build();
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
}
