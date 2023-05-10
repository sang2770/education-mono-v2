package com.sang.nv.education.exam.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//            End point for client to connect
        registry.addEndpoint("/socket").setAllowedOriginPatterns("*").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//            End point for client to subscribe
        registry.enableSimpleBroker("/topic");
//            End point for client to send message
        registry.setApplicationDestinationPrefixes("/exam");
    }
}
