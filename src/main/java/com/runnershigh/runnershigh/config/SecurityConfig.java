package com.runnershigh.runnershigh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    //비밀번호 암호화를 위한 Bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // CORS
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);          // 모든 출처 허용
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);                 // 모든 HTTP 메서드 허용
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);                 // 모든 헤더 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());        //위에서 만든 Cors 적용
        http.csrf(csrf -> csrf.disable());                     //CSRF 보호 비활성화
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.logout(logout -> logout.disable());
        //무상태 방식 사용
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //필터 적용
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // SCRF 보호 비활성화
        // 쿠키 기반 인증일 때 문제가 되고, JWT 쓰는 경우 일반적으로 비활성화
        http.csrf(csrf -> csrf.disable())
            // 모든 HTTP 요청에 대해 접근 허용
            .authorizeHttpRequests(auth -> {
                auth.anyRequest().permitAll(); // 어떤 요청이든 전부 허용
            });

        //OAuth2 설정 추가
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2PrincipalUserService))
                .successHandler(oAuth2SuccessHandler)
        );


        return http.build();
    }
}
