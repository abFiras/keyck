package com.example.notification.service;

import com.example.notification.config.KeycloakConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private KeycloakConfig keycloakConfig;

    public String getUserServiceToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakConfig.getUserServiceUrl() + "/token";
        return restTemplate.getForObject(url, String.class);
    }
}