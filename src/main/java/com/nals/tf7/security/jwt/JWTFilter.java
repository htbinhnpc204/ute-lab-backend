package com.nals.tf7.security.jwt;

import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.service.v1.RedisService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Objects;

import static com.nals.tf7.security.jwt.TokenProvider.REFRESH_TOKEN_ID;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter
    extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private  final RedisService redisService;

    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String jwtToken = tokenProvider.resolveToken(request);
        Claims claims = tokenProvider.validateAndGet(jwtToken);

        if (Objects.nonNull(claims) && claims.containsKey(REFRESH_TOKEN_ID) && jwtToken != null) {
            String oldToken = (String) redisService.getValue(jwtToken);
            if (StringHelper.isBlank(oldToken) || !oldToken.equals(jwtToken)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
