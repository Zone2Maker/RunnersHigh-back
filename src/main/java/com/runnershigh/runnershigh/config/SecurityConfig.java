package com.runnershigh.runnershigh.config;

import com.runnershigh.runnershigh.security.Filter.JwtAuthenticationFilter;
import com.runnershigh.runnershigh.security.handler.OAuth2SuccessHandler;
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
    public JwtAuthenticationFilter jwtAuthenticationFilter;


//    @Autowired
//    private OAuth2SuccessHandler oAuth2SuccessHandler;

    //TODO: OAuth2PrincipalUserService 구현 후 의존성 주입

    //비밀번호 암호화를 위한 Bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // CORS
    @Bean
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
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/**", "/user/check", "/oauth2/**", "/auth/oauth2/**").permitAll()    //요청주소 - 허용할 주소
            // 피드, 크루 조회 여부에 따라 변경
            .requestMatchers(HttpMethod.GET, "/feed/**", "/crew/**").permitAll()
            .anyRequest().authenticated();                 //위에 주소 제외 모든 요청은 인증필요
        });

        //TODO: OAuth2 설정 추가



        return http.build();
    }
}
