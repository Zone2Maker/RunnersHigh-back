package com.runnershigh.runnershigh.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key KEY;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .subject("AccessToken")
                .id(userId)
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L * 30L)))
                .signWith(KEY)
                .compact();
    }

    public boolean isBearer(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public String removeBearer(String token) {
        return token.replaceFirst("Bearer ", "");
    }

    public Claims getClaims(String token) throws JwtException {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();
        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
