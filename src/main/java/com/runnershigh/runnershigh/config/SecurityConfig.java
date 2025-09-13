package com.runnershigh.runnershigh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // CORS


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // SCRF 보호 비활성화
        // 쿠키 기반 인증일 때 문제가 되고, JWT 쓰는 경우 일반적으로 비활성화
        http.csrf(scrf -> scrf.disable())
            // 모든 HTTP 요청에 대해 접근 허용
            .authorizeHttpRequests(auth -> {
                auth.anyRequest().permitAll(); // 어떤 요청이든 전부 허용
            });


        return http.build();
    }
}
