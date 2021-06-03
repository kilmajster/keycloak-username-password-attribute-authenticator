package io.github.kilmajster.keycloak.base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public final class Steps {

    protected static final Logger log = LoggerFactory.getLogger(Steps.class);


    public static void go_to_keycloak_account_page(final String keycloakUrl) {
        log.info("[TEST] go_to_keycloak_account_page( keycloakUrl = " + keycloakUrl + " )");

        open(keycloakUrl + "/auth/realms/dev-realm/account");
    }

    public static void click_sign_in_button() {
        log.info("[TEST] click_sign_in_button()");

        $(By.id("landingSignInButton")).click();
    }

    public static void verify_login_form_is_displayed_with_user_attribute_label(final String label) {
        log.info("[TEST] verify_login_form_is_displayed_with_user_attribute_label( label = " + label + " )");

        assertThat($(By.id("kc-form-login")).isDisplayed()).isTrue();
        userAttributeFormLabel().shouldHave(text(label));
    }

    public static void log_into_account_console() {
        log.info("[TEST] log_into_account_console()");

        $(By.id("username")).val("test");
        $(By.id("password")).val("test");
        $(By.id("login_form_user_attribute")).val("test");
        $(By.id("kc-login")).click();
    }

    public static void verify_that_user_is_logged_in() {
        log.info("[TEST] verify_that_user_is_logged_in()");

        $(By.id("landingLoggedInUser")).shouldHave(text("test"));
    }

    private static SelenideElement userAttributeFormLabel() {
        return $(By.xpath("//input[@id='login_form_user_attribute']/preceding-sibling::label"));
    }
}
