package com.example.notification.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Ajout de l'intercepteur pour valider les tokens JWT
        registration.interceptors(webSocketAuthInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200") // Autorise les connexions depuis l'origine spécifiée
                .withSockJS(); // Activer SockJS pour la compatibilité avec les anciens navigateurs
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configurer le broker pour les messages
        registry.enableSimpleBroker("/topic"); // Les messages publiés sur /topic seront envoyés aux abonnés
        registry.setApplicationDestinationPrefixes("/app"); // Préfixe pour les messages envoyés par le client
    }
}

