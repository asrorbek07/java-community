package uz.community.javacommunity.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.community.javacommunity.common.constants.JwtConstants;
import uz.community.javacommunity.controller.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtService {
    public static final Duration JWT_TOKEN_VALIDITY = Duration.of(1, ChronoUnit.HOURS);

    @Value("EFBEF594651CB99ABDAE372F52C22GDSLSADDSSDF")
    private String secret;

    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now.toEpochMilli()))
                .setExpiration(new Date(now.plus(JWT_TOKEN_VALIDITY).toEpochMilli()))
                .addClaims(Map.of(
                        JwtConstants.ROLES_CLAIM, user.getRoles())
                )
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> decode(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build();
        return jwtParser.parseClaimsJws(token);
    }

    public String getUsernameFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (Objects.isNull(token))
            return null;
        Jws<Claims> claimsJws = decode(token.substring(7));
        return claimsJws.getBody().getSubject();
    }
}
