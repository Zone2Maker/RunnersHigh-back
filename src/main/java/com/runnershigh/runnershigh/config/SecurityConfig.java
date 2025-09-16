package com.runnershigh.runnershigh.config;

import com.runnershigh.runnershigh.security.filter.JwtAuthenticationFilter;
import com.runnershigh.runnershigh.security.handler.OAuth2SuccessHandler;
import com.runnershigh.runnershigh.service.OAuth2PrincipalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    private OAuth2PrincipalUserService oAuth2PrincipalUserService;

    // 비밀번호 암호화(해싱), 검증
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS
    // 외부 출처 요청을 허용하기 위한 CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 정책 생성
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);          // 모든 출처(도메인) 허용
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);                 // 모든 HTTP 메서드 허용
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);                 // 모든 헤더 허용

        // 모든 URL(/**)에 위에서 만든 CORS 정책 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 각각의 url 패턴에 다른 corsConfiguration 적용이 가능하다.
        // ex) .registerCorsConfiguration("admin/**", configB); -> 관리자 페이지만 다른 CORS 적용이 가능

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());        //위에서 만든 CORS 적용
        http.csrf(csrf -> csrf.disable()); //CSRF 보호 비활성화

        // JWT 쓰므로 기본 폼 로그인 비활성화
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.logout(logout -> logout.disable());

        // JWT 방식은 세션이 필요없으므로..
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 커스텀 필터를 보안 필터 체인에 끼워넣기
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // SCRF 보호 비활성화
        // 쿠키 기반 인증일 때 문제가 되고, JWT 쓰는 경우 일반적으로 비활성화
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/ws/**").permitAll() // 웹 소켓 첫 연결 -> 인증 안된 상태
                    .requestMatchers("/auth/**", "/user/check", "/oauth2/**", "/auth/oauth2/**").permitAll()    // 요청주소 - 허용할 주소
                    // 피드, 크루 조회 여부에 따라 변경
                    .requestMatchers(HttpMethod.GET, "/feed/**", "/crew/**", "/feed/weekly-top", "/crew/weekly-top").permitAll()
                    .requestMatchers(HttpMethod.GET, "/auth/principal").authenticated()
                    .anyRequest().authenticated(); // 위에 주소 제외 모든 요청은 인증 필요
        });

        // 소셜 로그인 활성화
        http.oauth2Login(oauth2 ->
                // 로그인에 성공한 사용자의 정보를 가져온 후 처리 과정
                oauth2.userInfoEndpoint(userInfo ->
                        userInfo.userService(oAuth2PrincipalUserService))
                        // 처리 과정이 끝난 후의 실행 로직
                        .successHandler(oAuth2SuccessHandler));
        // Spring Security가 만들어주는 기본 로그인 주소
        // http://localhost:8080/oauth2/authorization

        return http.build();
    }
}
