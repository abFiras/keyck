package com.example.userservice.Service;

import com.example.userservice.Dto.UserRegistrationRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord);
     List<RoleRepresentation> getUserRoles(String userId) ;

        UserRepresentation getUserById(String userId);
    void deleteUserById(String userId );
    void emailVerification(String userId);
    UserResource getUserResource(String userId);
    void updatePassword(String userId);
    void sendPasswordResetEmail(String email);
    void forgetpassword(String username);
     Boolean isAdmin(String userId);
    String getAccessToken();
     String getUserIdFromToken(String accessToken) ;
    String getUsernameByUserId(String userId);
}

