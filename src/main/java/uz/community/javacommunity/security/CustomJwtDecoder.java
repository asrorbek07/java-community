package uz.community.javacommunity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import uz.community.javacommunity.common.constants.JwtConstants;
import uz.community.javacommunity.service.JwtService;

import java.time.Instant;

@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtService jwtService;

    @Override
    public Jwt decode(String token) throws JwtException {
        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtService.decode(token);
        } catch (io.jsonwebtoken.JwtException ex) {
            throw new BadJwtException(ex.getMessage(), ex);
        }
        Claims claims = claimsJws.getBody();

        Jwt.Builder builder = Jwt.withTokenValue(token)
                .subject(claims.getSubject())
                .issuedAt(Instant.ofEpochMilli(claims.getIssuedAt().getTime()))
                .expiresAt(Instant.ofEpochMilli(claims.getExpiration().getTime()))
                .claim(JwtConstants.ROLES_CLAIM, claims.get(JwtConstants.ROLES_CLAIM))
                .header(JwsHeader.ALGORITHM, claimsJws.getHeader().getAlgorithm());
        return builder.build();
    }
}
