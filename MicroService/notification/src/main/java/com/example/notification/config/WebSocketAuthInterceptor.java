package com.example.notification.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
                .replace("Bearer ", "");

        // Valider le token JWT ici (exemple avec Keycloak)
        if (!validateToken(token)) {
            throw new AccessDeniedException("Invalid token");
        }

        return message;
    }

    private boolean validateToken(String token) {
        // Logique de validation du token JWT
        return true; // Changez avec votre implémentation réelle
    }

}
