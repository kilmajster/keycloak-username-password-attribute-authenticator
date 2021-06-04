package io.github.kilmajster.keycloak;

import com.codeborne.selenide.SelenideElement;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;

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

    @Given("keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = {string}")
    public void keycloak_is_running_with_login_form_attribute_label_env(final String envLoginFormAttributeLabel) {
        keycloak.addEnv("LOGIN_FORM_ATTRIBUTE_LABEL", envLoginFormAttributeLabel);
        if (!keycloak.isRunning()) {
            log.info("Starting keycloak container with LOGIN_FORM_ATTRIBUTE_LABEL = " + envLoginFormAttributeLabel);
            keycloak.start();
        }
    }

    @Given("keycloak is running with LOGIN_FORM_GENERATE_LABEL = {}")
    public void keycloak_is_running_with_login_form_generate_label_env(final boolean envLoginFormGenerateLabel) {
        keycloak.addEnv("LOGIN_FORM_GENERATE_LABEL", envLoginFormGenerateLabel ? "true": "false");
        if (!keycloak.isRunning()) {
            log.info("Starting keycloak container with LOGIN_FORM_GENERATE_LABEL = " + envLoginFormGenerateLabel);
            keycloak.start();
        }
    }

    @Given("keycloak is running with LOGIN_FORM_USER_ATTRIBUTE = {string}")
    public void keycloak_is_running_with_login_form_user_attribute_env(final String envLoginFormUserAttribute) {
        keycloak.addEnv("LOGIN_FORM_USER_ATTRIBUTE", envLoginFormUserAttribute);
        if (!keycloak.isRunning()) {
            log.info("Starting keycloak container with LOGIN_FORM_USER_ATTRIBUTE = " + envLoginFormUserAttribute);
            keycloak.start();
        }
    }

    @When("user goes to the account console page")
    public void go_to_keycloak_account_page() {
        final String keycloakUrl = TestConstants.KEYCLOAK_LOCAL_URL_PREFIX + keycloak.getFirstMappedPort();
        open(keycloakUrl + "/auth/realms/dev-realm/account");
    }

    @Then("user should be not logged in")
    public void user_should_be_not_logged_in() {
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

    private SelenideElement userAttributeFormLabel() {
        return $(By.xpath("//input[@id='login_form_user_attribute']/preceding-sibling::label"));
    }
}
