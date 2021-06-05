package io.github.kilmajster.keycloak;

import com.codeborne.selenide.SelenideElement;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.github.kilmajster.keycloak.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public final class KeycloakSteps {

    private static final Logger log = LoggerFactory.getLogger(KeycloakSteps.class);

    private final KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_DEV_DOCKER_IMAGE)
            .withRealmImportFile("dev-realm.json")
            .withLogConsumer(new Slf4jLogConsumer(log));

    @Given("keycloak is running with default setup")
    public void keycloak_is_running_with_default_setup() {
        if (!keycloak.isRunning()) {
            log.info("Starting keycloak container...");
            keycloak.start();
        }
    }

    @Given("keycloak is running with {} = {}")
    public void keycloak_is_running_with_env(final String envKey, final String envValue) {
        keycloak.addEnv(envKey, envValue);
        if (!keycloak.isRunning()) {
            log.info("Starting keycloak container with " + envKey + " = " + envValue);
            keycloak.start();
        }
    }

    @When("user goes to the account console page")
    public void go_to_keycloak_account_page() {
        final String keycloakUrl = TestConstants.KEYCLOAK_LOCAL_URL_PREFIX + keycloak.getFirstMappedPort();

        log.info("go_to_keycloak_account_page() :: keycloakUrl = " + keycloakUrl);

        open(keycloakUrl + "/auth/realms/dev-realm/account");
    }

    @Then("user should be not logged in")
    public void user_should_be_not_logged_in() {
        log.info("user_should_be_not_logged_in()");

        final String loggedInUser = $(By.id("landingLoggedInUser")).val();
        assertThat(loggedInUser).isNullOrEmpty();
    }

    @When("user clicks a sign in button")
    public void click_sign_in_button() {
        log.info("click_sign_in_button()");

        $(By.id("landingSignInButton")).click();
    }

    @When("user navigates to login page")
    public void user_navigates_to_login_page() {
        log.info("user_navigates_to_login_page()");

        go_to_keycloak_account_page();
        user_should_be_not_logged_in();
        click_sign_in_button();
    }

    @Then("login form with attribute input labeled as {string} should be shown")
    public void verify_login_form_is_displayed_with_user_attribute_label(final String label) {
        log.info("verify_login_form_is_displayed_with_user_attribute_label( label = " + label + " )");

        assertThat($(By.id("kc-form-login")).isDisplayed()).isTrue();
        userAttributeFormLabel().shouldHave(text(label));
    }

    @When("user log into account console with a valid credentials and user attribute equal {string}")
    public void log_into_account_console(final String attribute) {
        log.info("log_into_account_console()");

        $(By.id("username")).val(TEST_USERNAME);
        $(By.id("password")).val(TEST_PASSWORD);
        $(By.id("login_form_user_attribute")).val(attribute);
        $(By.id("kc-login")).click();
    }

    @Then("user should be logged into account console")
    public void verify_that_user_is_logged_in() {
        log.info("verify_that_user_is_logged_in()");

        $(By.id("landingLoggedInUser")).shouldHave(text("test"));
    }

    @Then("form error with message {string} is present")
    public void form_error_with_message_is_present(final String errorMessage) {
        log.info("form_error_with_message_is_present( " + errorMessage + " )");

        $(By.className("alert-error")).shouldHave(text(errorMessage));
    }

    @And("attempted username is cleared")
    public void attempted_username_is_cleared() {
        log.info("attempted_username_is_cleared()");

        assertThat($(By.id("kc-attempted-username")).exists()).isFalse();
    }

    @And("attempted username is set to {string}")
    public void attempted_username_is_set_to(final String attemptedUsername) {
        log.info("attempted_username_is_set_to( " + attemptedUsername + " )");

        $(By.id("kc-attempted-username")).shouldHave(exactText(attemptedUsername));
    }

    @And("restart login link is visible")
    public void restart_login_link_is_visible() {
        log.info("restart_login_link_is_visible()");

        assertThat($(By.className("kc-tooltip-text")).getOwnText()).isEqualTo("Restart login");
    }

    private SelenideElement userAttributeFormLabel() {
        return $(By.xpath("//input[@id='login_form_user_attribute']/preceding-sibling::label"));
    }
}