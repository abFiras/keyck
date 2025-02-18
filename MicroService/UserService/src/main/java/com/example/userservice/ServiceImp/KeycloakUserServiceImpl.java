package com.example.userservice.ServiceImp;
import com.example.userservice.Dto.UserRegistrationRecord;
import com.example.userservice.Repository.UserRepo;
import com.example.userservice.Service.KeycloakUserService;

import com.example.userservice.exception.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class KeycloakUserServiceImpl implements KeycloakUserService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakUserServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

        private static final Logger log = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

    @Value("${keycloak.urls.auth}")
    private String keycloakAuthServerUrl;


    @Override
    public String getUserIdFromToken(String accessToken) {
        // Créer une instance de RestTemplate pour effectuer des requêtes HTTP
        RestTemplate restTemplate = new RestTemplate();

        // URL de l'API /userinfo de Keycloak
        String userInfoUrl = keycloakAuthServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

        // Créer les en-têtes HTTP, en ajoutant le token d'accès dans l'en-tête Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Créer une requête GET pour l'API userinfo
        ResponseEntity<String> response = null;
        try {
            // Utilisation correcte de RestTemplate.exchange
            response = restTemplate.exchange(
                    userInfoUrl, // URL de l'API
                    HttpMethod.GET, // Type de la requête (GET)
                    new org.springframework.http.HttpEntity<>(headers), // Entité avec les en-têtes
                    String.class); // Type de la réponse attendu
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à l'API Keycloak /userinfo", e);
        }

        // Extraire l'ID utilisateur (souvent dans le champ "sub" dans la réponse JSON)
        String responseBody = response.getBody();
        // Extraire l'ID utilisateur du JSON
        String userId = extractUserIdFromJson(responseBody);
        return userId;
    }

    // Méthode pour extraire l'ID utilisateur du corps de la réponse JSON
    private String extractUserIdFromJson(String jsonResponse) {
        // Utiliser Jackson pour analyser la réponse JSON
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("sub").asText(); // Le champ "sub" contient l'ID utilisateur
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'extraction de l'ID utilisateur depuis le JSON", e);
        }
    }      /*  @Override
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
*/
//        response.readEntity()
@Override
public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {
    try {
        // Création de l'utilisateur dans Keycloak
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

            // Récupérer l'utilisateur créé
            List<UserRepresentation> representationList = usersResource.searchByUsername(userRegistrationRecord.getUsername(), true);
            if (!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation createdUser = representationList.stream()
                        .filter(u -> u.getUsername().equals(userRegistrationRecord.getUsername()))
                        .findFirst().orElse(null);

                if (createdUser != null) {
                    // Récupérer le rôle de l'utilisateur à partir du token (resource_access)
                    List<String> roles = getRolesFromResourceAccess(userRegistrationRecord.getRole());

                    // Ajouter le rôle à l'utilisateur
                    addRoleToUser(createdUser.getId(), roles);

                    // Envoi de l'email pour vérifier l'adresse
                    if (!createdUser.isEmailVerified()) {
                        emailVerification(createdUser.getId());
                        log.info("Email sent to user id {}", createdUser.getId());
                    }
                }
            }
            return userRegistrationRecord;
        } else if (response.getStatus() == 409) {
            throw new UserAlreadyExistsException("User or email already exists");
        } else {
            log.error("Failed to create user in Keycloak. Status: " + response.getStatus() + ", Message: " + response.readEntity(String.class));
            return null;
        }
    } catch (Exception e) {
        throw new UserAlreadyExistsException("User or email already exists");
    }
}
    @Override
    public List<RoleRepresentation> getUserRoles(String userId) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId.toString());
        return userResource.roles().realmLevel().listAll();
    }
    @Override
    public Boolean isAdmin(String userId){
            List<RoleRepresentation> Roles= getUserRoles(userId);
            for (RoleRepresentation  role:Roles){
                if("admin".equals(role.getName())){
                    return true;
                }

            }
            return false;
    }
    private List<String> getRolesFromResourceAccess(String roleName) {
        // Dans ce cas, récupérer les rôles d'un client spécifique à partir de resource_access dans le token
        List<String> roles = new ArrayList<>();
        // Exemple d'un rôle spécifique à un client (clientId '123')
        if ("123".equals(roleName)) {
            // Récupérer les rôles associés à ce client dans resource_access
            roles.add("proprietaire");
            roles.add("malek");
            roles.add("admin");
        }
        return roles;
    }

    private void addRoleToUser(String userId, List<String> roles) {
        // Récupérer les rôles depuis Keycloak
        for (String roleName : roles) {
            RoleRepresentation role = getRoleRepresentation(roleName);
            if (role != null) {
                // Ajouter le rôle à l'utilisateur
                UsersResource usersResource = getUsersResource();
                usersResource.get(userId).roles().realmLevel().add(Arrays.asList(role));
                log.info("Role {} added to user {}", roleName, userId);
            } else {
                log.error("Role {} not found in Keycloak", roleName);
            }
        }
    }

    private RoleRepresentation getRoleRepresentation(String roleName) {
        // Récupérer le rôle à partir du nom dans le royaume Keycloak
        RolesResource rolesResource = getRealmResource().roles();
        RoleRepresentation role = null;
        try {
            role = rolesResource.get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            log.error("Role {} not found in Keycloak", roleName);
        }
        return role;
    }
    private RealmResource getRealmResource() {
        return keycloak.realm("malek"); // Remplace "your-realm-name" par le nom réel de ton realm Keycloak
    }

    @Override
    public String getUsernameByUserId(String userId) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            return user.getUsername();
        } catch (NotFoundException e) {
            throw new RuntimeException("User not found for ID: " + userId);
        }
    }

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

    @Override
    public String getAccessToken() {
        try {
            return keycloak.tokenManager().getAccessToken().getToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch access token: " + e.getMessage(), e);
        }
    }

}
