package com.example.annonce.Iservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Service
@FeignClient(name = "user", url = "http://localhost:8088/users")
public interface UserServiceClient {

    @GetMapping("/{accessToken}")  // Corrected URL pattern
    String getUserById(@PathVariable("accessToken") String accessToken);
}
