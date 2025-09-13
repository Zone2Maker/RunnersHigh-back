package com.runnershigh.runnershigh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   // websocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // STOMP(Simple Text Oriented Messaging Protocol)
    // WebSocket 위 텍스트 기반 메시징 프로토콜 (PUB/SUB 기반)
    // @MessageMapping으로 메시지 발행 시 엔드포인트 별도 분리 가능

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 주소, 서버 입장에서는 보내는 곳
        registry.enableSimpleBroker("/sub");
        // 클라이언트가 메시지를 발행할 주소, 서버 입장에서는 받는 곳
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // WebSocket에 접속하기 위한 endpoint 설정
    // 도메인이 다른 서버에서도 접속 가능하도록 CORS 설정
    // 클라이언트는 http가 아닌 ws로 연결, 통신
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트
        // 순수 WebSocket 연결, 다른 도메인에서도 접근 허용
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        // sockjs는 WebSocket을 지원하지 않는 버전의 브라우저에서
                // WebSocket을 사용할 수 있게 해주는 라이브러리
    }
}
