package io.github.kilmajster.keycloak;


import com.codeborne.selenide.WebDriverRunner;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;


public class UsernamePasswordAttributeFormAT extends BaseKeycloakAT {

    @Rule
    public KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json");

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withCapabilities(DesiredCapabilities.chrome());

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