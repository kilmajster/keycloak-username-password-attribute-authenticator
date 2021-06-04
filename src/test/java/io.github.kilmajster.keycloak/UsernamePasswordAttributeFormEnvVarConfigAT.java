package io.github.kilmajster.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.github.kilmajster.keycloak.base.BaseKeycloakAT;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static io.github.kilmajster.keycloak.base.Steps.*;


public class UsernamePasswordAttributeFormEnvVarConfigAT extends BaseKeycloakAT {

    @Rule
    public KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json")
            .withEnv("LOGIN_FORM_ATTRIBUTE_LABEL", "Custom label")
            .withLogConsumer(new Slf4jLogConsumer(log));

    @Test
    public void shouldTakeAttributeLabelFromEnvVariable() {
        final String keycloakBaseUrl = getKeycloakBaseUrl(keycloak);

        go_to_keycloak_account_page(keycloakBaseUrl);
        click_sign_in_button();
        verify_login_form_is_displayed_with_user_attribute_label("Custom label");
        log_into_account_console();
        verify_that_user_is_logged_in();
    }
}