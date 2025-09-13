package com.runnershigh.runnershigh.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
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
    //토큰 만들기 - JWT 엑세스 토큰 문자열로 반환
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .subject("AccessToken")
                .id(userId)
                .setExpiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L * 30L)))
                .signWith(KEY)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        if(token == null) {
            return false;
        }
        if (!token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    //removeBearer - 순수 토큰만 추출
    public String removeBearer(String token) {
        return token.replaceFirst("Bearer ", "");
    }

    //토큰에서 사용자 정보 Claims 로 가져오기
    public Claims getClaims(String token) {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();
        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();
    }


}
