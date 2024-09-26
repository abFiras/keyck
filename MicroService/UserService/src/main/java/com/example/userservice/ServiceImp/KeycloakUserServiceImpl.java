package com.example.userservice.ServiceImp;
import com.example.userservice.Dto.UserRegistrationRecord;
import com.example.userservice.Repository.UserRepo;
import com.example.userservice.Service.KeycloakUserService;

import com.example.userservice.exception.UserAlreadyExistsException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@RequiredArgsConstructor
@Service
public class KeycloakUserServiceImpl implements KeycloakUserService {
    @Autowired
    private UserRepo userRepository;
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


        private static final Logger log = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

        @Override
        public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {
            try {
                UserRepresentation user = new UserRepresentation();
                user.setEnabled(true);
                user.setUsername(userRegistrationRecord.getUsername());
                user.setEmail(userRegistrationRecord.getEmail());
                user.setFirstName(userRegistrationRecord.getFirstName());
                user.setLastName(userRegistrationRecord.getLastName());
                user.setEmailVerified(false);

                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setValue(userRegistrationRecord.getPassword());
                credentialRepresentation.setTemporary(false);
                credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

                List<CredentialRepresentation> list = new ArrayList<>();
                list.add(credentialRepresentation);
                user.setCredentials(list);

                UsersResource usersResource = getUsersResource();
                Response response = usersResource.create(user);
                log.info("Keycloak create user response: " + response.getStatus());

                if (response.getStatus() == 201) {
                    log.info("User created successfully in Keycloak.");

                    List<UserRepresentation> representationList = usersResource.searchByUsername(userRegistrationRecord.getUsername(), true);
                    if (!CollectionUtils.isEmpty(representationList)) {
                        UserRepresentation userRepresentation1 = representationList.stream()
                                .filter(userRepresentation -> Objects.equals(false, userRepresentation.isEmailVerified()))
                                .findFirst().orElse(null);
                        assert userRepresentation1 != null;
                        emailVerification(userRepresentation1.getId());
                        log.info("Email sent to user id {}", userRepresentation1.getId());
                    }
                    return userRegistrationRecord;
                } else if (response.getStatus() == 409) {
                    throw new UserAlreadyExistsException("User or email already exists");

                } else {
                    log.error("Failed to create user in Keycloak. Status: " + response.getStatus() + ", Message: " + response.readEntity(String.class));
                    return null;
                }
        }catch (Exception e){
                throw new UserAlreadyExistsException("User or email already exists");
            }

            }

//        response.readEntity()



    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId) {
        // Valider le mot de passe
            getUsersResource().delete(userId);
            System.out.println("avec succes");

            throw new RuntimeException("Mot de passe incorrect.");

    }

    // Méthode pour valider le mot de passe
    private boolean validatePassword(String userId, String password) {
        // Extraire le nom d'utilisateur à partir de l'ID utilisateur
        String username = getUsernameFromUserId(userId);

        // Authentifier l'utilisateur avec le mot de passe
        try {
             KeycloakBuilder.builder()
                    .serverUrl("${keycloak.urls.auth}")
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId("${keycloak.adminClientId}")
                    .clientSecret("${keycloak.adminClientSecret}")
                     .username(username)
                     .password(password)
                    .build();

            // Tenter de récupérer le token d'accès
            keycloak.tokenManager().getAccessToken();
            return true; // Mot de passe correct
        } catch (Exception e) {
            return false; // Mot de passe incorrect
        }
    }

    private String getUsernameFromUserId(String userId) {
        try {
            // Obtenir la ressource des utilisateurs
            UserResource userResource = getUsersResource().get(userId);
            // Récupérer les détails de l'utilisateur
            UserRepresentation userRepresentation = userResource.toRepresentation();
            // Retourner le nom d'utilisateur
            return userRepresentation.getUsername();
        } catch (NotFoundException e) {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId);
        } catch (Exception e) {
            // Gérer d'autres exceptions potentielles
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }
    }

    @Override
    public void emailVerification(String userId) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }
    //        List<String> actions = new ArrayList<>();
//        actions.add("UPDATE_PASSWORD");
    @Override
    public void updatePassword(String userId) {
        UserResource userResource = getUserResource(userId);

        userResource.executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }
    public void sendPasswordResetEmail(String email) {
        UserResource userResource = getUserResourceByEmail(email);

        // Envoie un email pour mettre à jour le mot de passe
        userResource.executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    private UserResource getUserResourceByEmail(String email) {
        RealmResource realmResource = keycloak.realm(realm);
        return (UserResource) realmResource.users().search(email).get(0);
    }
    public UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public void forgetpassword(String username) {
        UsersResource usersResource=getUsersResource();
        List<UserRepresentation>representationList=usersResource.searchByUsername(username,true);
        UserRepresentation userRepresentation=representationList.stream().findFirst().orElse(null);

        if (userRepresentation !=null){
            UserResource userResource=usersResource.get(userRepresentation.getId());
            List<String> actions=new ArrayList<>();
            actions.add("UPDATE_PASSWORD");
            userResource.executeActionsEmail(actions);
            return;

        }
        throw new RuntimeException("username not found ");

    }


}
