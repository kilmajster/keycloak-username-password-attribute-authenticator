package io.github.kilmajster.keycloak.base;

import com.codeborne.selenide.WebDriverRunner;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public abstract class BaseKeycloakInDockerAT {

    protected static final Logger log = LoggerFactory.getLogger(BaseKeycloakInDockerAT.class);

    protected static final String KEYCLOAK_DEV_DOCKER_IMAGE = "kilmajster/keycloak-with-authenticator:test";
    protected static final String KEYCLOAK_NETWORK_ALIAS = "keycloak-host";
    protected static final int KEYCLOAK_DEFAULT_PORT = 8080;
    protected static final String KEYCLOAK_DOCKER_URL = "http://" + KEYCLOAK_NETWORK_ALIAS + ":" + KEYCLOAK_DEFAULT_PORT;
    protected static final String KEYCLOAK_LOCAL_URL_PREFIX = "http://localhost:";

    @Rule
    public Network testNetwork = isHeadless() ? Network.newNetwork() : null;

    @Rule
    public BrowserWebDriverContainer chrome = isHeadless() ? (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withNetwork(testNetwork)
            .withLogConsumer(new Slf4jLogConsumer(log)) : null;

    @Before
    public void setUp() {
        if (isHeadless()) {
            log.info("Headless mode is enabled!");
            RemoteWebDriver driver = chrome.getWebDriver();
            WebDriverRunner.setWebDriver(driver);
        }
    }

    @After
    public void tearDown() {
        if (isHeadless()) {
            WebDriverRunner.closeWebDriver();
        }
    }

    protected static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless"));
    }

    protected String getKeycloakBaseUrl(final KeycloakContainer keycloakContainer) {
        return isHeadless() ? KEYCLOAK_DOCKER_URL : KEYCLOAK_LOCAL_URL_PREFIX + keycloakContainer.getFirstMappedPort();
    }
}