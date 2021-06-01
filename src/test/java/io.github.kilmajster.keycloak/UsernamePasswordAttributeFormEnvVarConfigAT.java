package io.github.kilmajster.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class UsernamePasswordAttributeFormEnvVarConfigAT extends BaseKeycloakAT {

    @Container
    private final KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json")
            .withEnv("LOGIN_FORM_ATTRIBUTE_LABEL", "Custom label");

    @Test
    public void shouldTakeAttributeLabelFromEnvVariable() {
        displayKeycloakAccountPage();
        clickSignInButton();
        verifyLoginFormIsDisplayedWithLabel("Custom label");
        logIntoAccountConsole();
        verifyThatUserIsLoggedIn();
    }

    @Override
    public KeycloakContainer keycloak() {
        return keycloak;
    }
}