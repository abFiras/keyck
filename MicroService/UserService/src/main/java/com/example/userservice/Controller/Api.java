package com.example.userservice.Controller;

import com.example.userservice.Dto.ResetPasswordRequest;
import com.example.userservice.Dto.UserRegistrationRecord;
import com.example.userservice.Service.KeycloakUserService;
import com.example.userservice.exception.UserAlreadyExistsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class Api {


    private final KeycloakUserService keycloakUserService;

    @GetMapping("/token")
    public String getToken() {
        String accessToken = keycloakUserService.getAccessToken();

        return accessToken;
    }

    @GetMapping("/{accessToken}")
    public String getUserId(@PathVariable("accessToken") String accessToken) {
        return keycloakUserService.getUserIdFromToken(accessToken);
    }
    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRecord userRegistrationRecord) {
        try {
            UserRegistrationRecord createdUser = keycloakUserService.createUser(userRegistrationRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable String userId) {
        try {
            List<RoleRepresentation> roles = keycloakUserService.getUserRoles(userId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/{userId}/isadmin")
    public Boolean isAdmin(@PathVariable String userId) {
        return keycloakUserService.isAdmin(userId);

    }

    @GetMapping("/{userId}/username")
    public ResponseEntity<String> getUsername(@PathVariable String userId) {
        try {
            String username = keycloakUserService.getUsernameByUserId(userId);
            return ResponseEntity.ok(username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        }
    }

    @GetMapping
    public UserRepresentation getUser(Principal principal) {

        return keycloakUserService.getUserById(principal.getName());
    }


    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable String userId) {
        keycloakUserService.deleteUserById(userId);
    }


    @PutMapping("/{userId}/send-verify-email")
    public void sendVerificationEmail(@PathVariable String userId) {
        keycloakUserService.emailVerification(userId);
    }

    @PutMapping("/update-password/{userId}")
    public void updatePassword(@PathVariable String userId) {
        keycloakUserService.updatePassword(userId);
    }

    /*  @PostMapping("/send-reset-password-email")
    public ResponseEntity<Void> sendPasswordResetEmail(@RequestParam String email) {
        keycloakUserService.sendPasswordResetEmail(email);
        return ResponseEntity.ok().build();
    }
  @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        keycloakUserService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String email) {
        List<UserRepresentation> users = keycloak.realm("malek").users().search(email);
        if (!users.isEmpty()) {
            UserRepresentation user = users.get(0);
            keycloak.realm("malek").users().get(user.getId()).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
        }
    }*/

    @PutMapping("/{username}/forgetpassowrd")
    public void forgetpassowrd (@PathVariable String username ){
        keycloakUserService.forgetpassword(username);
    }
}

