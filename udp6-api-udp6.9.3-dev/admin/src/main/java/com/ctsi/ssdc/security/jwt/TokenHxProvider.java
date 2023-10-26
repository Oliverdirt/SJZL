package com.ctsi.ssdc.security.jwt;


import com.ctsi.ssdc.config.CtsiProperties;
import com.ctsi.ssdc.security.CscpHxUserDetail;
import io.jsonwebtoken.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenHxProvider {

    private final Logger log = LoggerFactory.getLogger(TokenHxProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID_KEY = "id";
    private static final String TENANT_ID_KEY = "tenantId";
    private static final String TENANT_ACCOUNT = "tenantAccount";
    private static final String REMEMBERME_KEY = "rem";
    
    private final Base64.Encoder encoder = Base64.getEncoder();

    private String secretKey;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final CtsiProperties ctsiProperties;

    public TokenHxProvider(CtsiProperties ctsiProperties) {
        this.ctsiProperties = ctsiProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey = encoder.encodeToString(ctsiProperties.getSecurity().getAuthentication().getJwt()
            .getSecret().getBytes(StandardCharsets.UTF_8));

        this.tokenValidityInMilliseconds =
            1000 * ctsiProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * ctsiProperties.getSecurity().getAuthentication().getJwt()
                .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        
        JwtBuilder builder = Jwts.builder()
        		.setSubject(authentication.getName())
        		.claim(AUTHORITIES_KEY, authorities)
        		.claim(REMEMBERME_KEY, rememberMe ? 1 : 0);
        
        Object principal = authentication.getPrincipal();

        
        if(principal instanceof CscpHxUserDetail) {
        	Long id = ((CscpHxUserDetail)principal).getId();
        	builder.claim(USER_ID_KEY, id);
            Long tenantId = ((CscpHxUserDetail) principal).getTenantId();
            builder.claim(TENANT_ID_KEY, tenantId);
            String tenantAccount = ((CscpHxUserDetail) principal).getTenantAccount();
            builder.claim(TENANT_ACCOUNT, tenantAccount);
        }
        
        return builder
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        
        String authClaim = claims.get(AUTHORITIES_KEY).toString();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(authClaim.split(","))
                .filter(StringUtils::isNotEmpty)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long userId = NumberUtils.toLong(claims.get(USER_ID_KEY).toString());
        Long tenantId = NumberUtils.toLong(claims.get(TENANT_ID_KEY).toString());
        String tenantAccount = claims.get(TENANT_ACCOUNT).toString();

        CscpHxUserDetail principal = new CscpHxUserDetail(tenantId,tenantAccount,userId, claims.getSubject(), "", authorities);
        

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.debug("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.debug("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.debug("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.debug("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.debug("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
