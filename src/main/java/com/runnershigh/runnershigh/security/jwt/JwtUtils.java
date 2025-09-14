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

    //토큰 생성 - JWT 엑세스 토큰 문자열로 반환
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .subject("AccessToken")
                .id(userId)
                // 토큰 유효 기간: 30일
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L * 30L)))
                .signWith(KEY)
                .compact();
    }

    // Bearer 토큰 검증
    public boolean isBearer(String token) {
        return token != null && !token.startsWith("Bearer ");
    }

    // removeBearer - 순수 토큰만 추출
    public String removeBearer(String token) {
        return token.replaceFirst("Bearer ", "");
    }

    // JWT 토큰의 구조 - Header / Payload / Signature
    // Claims: JWT의 Payload 영역으로 사용자 정보, 만료 일자 등이 담겨있음
    // Signature: Header와 Payload를 비밀키로 암호화한 것
    // 클라이언트가 JWT 토큰과 함께 요청을 보내면 시크릿 키로 서버에 저장되어 있는 Signature와 비교
    // 불일치하면 -> 위조된 토큰으로 판단
    public Claims getClaims(String token) throws JwtException {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();  // 파서 빌더 생성
        jwtParserBuilder.setSigningKey(KEY);  // Signature 검증용 비밀키 설정
        JwtParser jwtParser = jwtParserBuilder.build();
        // 모든 검증, 예외 발생
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
