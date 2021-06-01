package io.github.kilmajster.keycloak;

import com.codeborne.selenide.SelenideElement;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseKeycloakAT {

    protected final static String KEYCLOAK_DEV_DOCKER_IMAGE = "kilmajster/keycloak-with-authenticator:test";

    public abstract KeycloakContainer keycloak();

    protected void displayKeycloakAccountPage() {
        open(keycloak().getAuthServerUrl() + "/realms/dev-realm/account");
    }

    protected void clickSignInButton() {
        $(By.id("landingSignInButton")).click();
    }

    protected void verifyLoginFormIsDisplayedWithLabel(final String label) {
        assertThat($(By.id("kc-form-login")).isDisplayed()).isTrue();
        userAttributeFormLabel().shouldHave(text(label));
    }

    protected SelenideElement userAttributeFormLabel() {
        return $(By.xpath("//input[@id='login_form_user_attribute']/preceding-sibling::label"));
    }

    protected void logIntoAccountConsole() {
        $(By.id("username")).val("test");
        $(By.id("password")).val("test");
        $(By.id("login_form_user_attribute")).val("test");
        $(By.id("kc-login")).click();
    }

    protected void verifyThatUserIsLoggedIn() {
        $(By.id("landingLoggedInUser")).shouldHave(text("test"));
    }

}
