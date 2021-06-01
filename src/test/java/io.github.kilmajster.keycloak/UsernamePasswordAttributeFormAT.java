package io.github.kilmajster.keycloak;


import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class UsernamePasswordAttributeFormAT extends BaseKeycloakAT {

    @Container
    private final KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json");

    @Test
    public void shouldLogIntoAccountConsole() {
        displayKeycloakAccountPage();
        clickSignInButton();
        verifyLoginFormIsDisplayedWithLabel("Test attr");
        logIntoAccountConsole();
        verifyThatUserIsLoggedIn();
    }

    @Override
    public KeycloakContainer keycloak() {
        return keycloak;
    }
}