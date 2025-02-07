package com.example.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecureApiClient {

    @Autowired
    private UserServiceClient userServiceClient;

    public String callSecureApi(String targetUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String token = userServiceClient.getUserServiceToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.postForObject(targetUrl, entity, String.class);
    }
}