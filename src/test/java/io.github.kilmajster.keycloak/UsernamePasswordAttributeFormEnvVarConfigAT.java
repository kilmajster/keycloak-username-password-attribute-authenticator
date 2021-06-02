package io.github.kilmajster.keycloak;

import com.codeborne.selenide.WebDriverRunner;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.github.kilmajster.keycloak.base.BaseKeycloakInDockerAT;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static io.github.kilmajster.keycloak.base.Steps.*;


public class UsernamePasswordAttributeFormEnvVarConfigAT extends BaseKeycloakInDockerAT {

    @Rule
    public KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json")
            .withEnv("LOGIN_FORM_ATTRIBUTE_LABEL", "Custom label")
            .withNetwork(testNetwork)
            .withNetworkAliases(KEYCLOAK_NETWORK_ALIAS)
            .withLogConsumer(new Slf4jLogConsumer(log));

    @Rule
    public BrowserWebDriverContainer chrome = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withNetwork(testNetwork)
            .withLogConsumer(new Slf4jLogConsumer(log));

    @Before
    public void setUp() {
        RemoteWebDriver driver = chrome.getWebDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @After
    public void tearDown() {
        WebDriverRunner.closeWebDriver();
    }

    @Test
    public void shouldTakeAttributeLabelFromEnvVariable() {
        go_to_keycloak_account_page(KEYCLOAK_TEST_URL);
        click_sign_in_button();
        verify_login_form_is_displayed_with_user_attribute_label("Custom label");
        log_into_account_console();
        verify_that_user_is_logged_in();
    }
}