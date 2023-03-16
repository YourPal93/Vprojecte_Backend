package com.friend.your.vprojecteapiserver.service;

import com.friend.your.vprojecte.vprojecteutils.dto.UserDto;
import jakarta.annotation.PostConstruct;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class KCServiceImpl {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientID;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private static Keycloak keycloak;
    private static RealmResource realmResource;
    private static UsersResource usersResource;

    @PostConstruct
    public Keycloak initKeycloak() {
        if(keycloak == null) {

            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverUrl)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();

            realmResource = keycloak.realm(realm);
            usersResource = realmResource.users();

            return keycloak;
        }

        return keycloak;
    }

    public Response createKeycloakUser(UserDto userDto) {

        var passwordCredentials = createPasswordCredentials(userDto.getPassword());

        var kcUser = new UserRepresentation();
        kcUser.setUsername(userDto.getUsername());
        kcUser.setCredentials(Collections.singletonList(passwordCredentials));
        kcUser.setEmail(userDto.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        var kcResponse = usersResource.create(kcUser);

        return kcResponse;
    }

    public UserRepresentation findKeycloakUserById(String userId) {

        return usersResource.get(userId).toRepresentation();
    }

    public void updateKeycloakUser(UserDto userDto) {

        var userResourceToUpdate = usersResource.get(userDto.getUserId());

        var credentials = createPasswordCredentials(userDto.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userDto.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentials));
        kcUser.setEmail(userDto.getEmail());


        userResourceToUpdate.update(kcUser);
    }

    public void deleteKeycloakUserById(String userId) {

        var userResourceToDelete = usersResource.get(userId);

        userResourceToDelete.remove();
    }

    public void addRolesToKeycloakUser(String userId, Collection<String> roles) {

        List<RoleRepresentation> rolesToAdd = new ArrayList<>();

        roles.forEach(role -> {
            RoleRepresentation realmRole = realmResource.roles().get(role).toRepresentation();
            rolesToAdd.add(realmRole);
        });

        var userResourceToUpdate = usersResource.get(userId);

        userResourceToUpdate.roles().realmLevel().add(rolesToAdd);
    }

    public CredentialRepresentation createPasswordCredentials(String password) {

        var passwordCredentials = new CredentialRepresentation();

        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }
}
