package com.nals.tf7.security.jwt;

import com.google.common.collect.ImmutableMap;
import com.nals.tf7.config.ApplicationProperties;
import com.nals.tf7.helpers.RandomHelper;
import com.nals.tf7.security.DomainUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.jhipster.config.JHipsterProperties;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class TokenProvider {

    public static final String ROLES_KEY = "roles";
    public static final String PERMISSIONS_KEY = "perms";
    public static final String USER_ID_KEY = "user_id";
    public static final String ACCESS_TOKEN_ID = "access_token_id";
    public static final String REFRESH_TOKEN_ID = "refresh_token_id";
    public static final String AUTHORIZATION_HEADER = "RW-Authorization";
    private static final int INFLATION_TIME_IN_MILLIS = 60000;

    private final Key key;
    private final JwtParser jwtParser;
    private final long accessTokenValidityInMillis;
    private final long refreshTokenValidityInMillis;

    public TokenProvider(final JHipsterProperties jHipsterProperties,
                         final ApplicationProperties applicationProperties) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (StringUtils.isNotBlank(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn("Warning: the JWT key used is not Base64-encoded");
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.accessTokenValidityInMillis = 1000 * applicationProperties.getAccessTokenValidityInSeconds();
        this.refreshTokenValidityInMillis = 1000 * applicationProperties.getRefreshTokenValidityInSeconds();
    }

    public Authentication getAuthentication(final String token) {
        Claims claims = parseClaimsJws(token);

        Collection<? extends GrantedAuthority> roles = getGrantedAuthority(claims, ROLES_KEY);
        Collection<? extends GrantedAuthority> permissions = getGrantedAuthority(claims, PERMISSIONS_KEY);

        DomainUserDetails principal = DomainUserDetails.builder()
                                                       .id(Long.valueOf(claims.get(USER_ID_KEY).toString()))
                                                       .username(claims.getSubject())
                                                       .roles(roles)
                                                       .authorities(permissions)
                                                       .build();

        return new UsernamePasswordAuthenticationToken(principal, token, permissions);
    }

    public Claims parseClaimsJws(final String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims parseClaimsJws(final HttpServletRequest request) {
        String token = resolveToken(request);
        if (Objects.isNull(token)) {
            return null;
        }
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(final HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims validateAndGet(final String token) {
        if (Strings.isBlank(token)) {
            return null;
        }

        try {
            log.info("Check token is valid or not");

            // Parse and check token is expired or not
            Claims claims = parseClaimsJws(token);

            String tokenId = claims.getId();
            Long userId = claims.get(USER_ID_KEY, Long.class);
            if (Objects.isNull(tokenId) || Objects.isNull(userId)) {
                return null;
            }

            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token trace: {}", e.getMessage());
            return null;
        }
    }

    public OAuthToken createOAuthToken(final Long userId,
                                       final String principalName,
                                       final String roles,
                                       final String permissions) {

        String accessTokenId = generateTokenId();
        String refreshTokenId = generateTokenId();
        Long issueAtMillis = System.currentTimeMillis();

        Token accessToken = createToken(accessTokenId, principalName, issueAtMillis,
                                        accessTokenValidityInMillis,
                                        ImmutableMap.of(ROLES_KEY, roles,
                                                        PERMISSIONS_KEY, permissions,
                                                        USER_ID_KEY, userId,
                                                        REFRESH_TOKEN_ID, refreshTokenId));

        Token refreshToken = createToken(refreshTokenId, principalName, issueAtMillis,
                                         refreshTokenValidityInMillis,
                                         ImmutableMap.of(ROLES_KEY, roles,
                                                         PERMISSIONS_KEY, permissions,
                                                         USER_ID_KEY, userId,
                                                         ACCESS_TOKEN_ID, accessTokenId));

        return OAuthToken.builder()
                         .accessToken(accessToken)
                         .refreshToken(refreshToken)
                         .build();
    }

    private String generateTokenId() {
        return RandomHelper.generateRandomAlphanumericString(80);
    }

    private String buildChecklistKey(final Long userId) {
        return String.format("checklist-%s", userId);
    }

    private String buildTokenKey(final Long userId, final String tokenId) {
        return UUID.nameUUIDFromBytes(String.format("%d-%s", userId, tokenId).getBytes()).toString();
    }

    private Token createToken(final String id,
                              final String principalName,
                              final Long issueAtMillis,
                              final Long validityMillis,
                              final Map<String, Object> claims) {

        JwtBuilder jwtBuilder = Jwts.builder()
                                    .setId(id)
                                    .setSubject(principalName)
                                    .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
                                    .signWith(key, SignatureAlgorithm.HS512)
                                    .setIssuedAt(new Date(issueAtMillis))
                                    .setNotBefore(new Date(issueAtMillis))
                                    .setExpiration(new Date(issueAtMillis + validityMillis));

        if (!CollectionUtils.isEmpty(claims)) {
            claims.forEach(jwtBuilder::claim);
        }

        return Token.builder()
                    .value(jwtBuilder.compact())
                    .expiredIn(issueAtMillis + validityMillis)
                    .build();
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthority(final Claims claims, final String claimKey) {
        return Arrays.stream(claims.get(claimKey).toString().split(","))
                     .filter(auth -> !auth.trim().isEmpty())
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }

    @Getter
    @Builder
    public static class OAuthToken {
        private Token accessToken;
        private Token refreshToken;
    }

    @Getter
    @Builder
    public static class Token {
        private String value;
        private long expiredIn;
    }
}
