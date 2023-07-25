package uz.community.javacommunity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import uz.community.javacommunity.common.constants.JwtConstants;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class WithJwtUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithAuthentication jwtAuthenticationInfo) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Jwt jwt = Jwt.withTokenValue("token value")
                .subject(jwtAuthenticationInfo.username())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.SECONDS))
                .claim(JwtConstants.ROLES_CLAIM, jwtAuthenticationInfo.roles())
                .header("typ", "JWT")
                .build();

        List<SimpleGrantedAuthority> authorities = Stream.of(jwtAuthenticationInfo.roles())
                .map(SimpleGrantedAuthority::new)
                .toList();

        Authentication auth = new JwtAuthenticationToken(jwt, authorities, jwtAuthenticationInfo.username());
        context.setAuthentication(auth);
        return context;
    }

}