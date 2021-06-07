package io.github.kilmajster.keycloak;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;
import java.util.Optional;

public interface UsernamePasswordAttributeFormConfiguration {

    String LOGIN_FORM_USER_ATTRIBUTE = "login_form_user_attribute";
    String LOGIN_FORM_GENERATE_LABEL = "login_form_generate_label";
    String LOGIN_FORM_ATTRIBUTE_LABEL = "login_form_attribute_label";
    String LOGIN_FORM_ERROR_MESSAGE = "login_form_error_message";
    String CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL = "clear_user_on_attribute_validation_fail";

    List<ProviderConfigProperty> PROPS = ProviderConfigurationBuilder.create()

            .property()
            .name(LOGIN_FORM_USER_ATTRIBUTE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute")
            .helpText("Attribute used to validate login form.")
            .add()

            .property()
            .name(LOGIN_FORM_GENERATE_LABEL)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
            .label("Generate label")
            .defaultValue("true") // only string value is accepted
            .helpText("If enabled, label for login form will be generated based on attribute name, so attribute with name:" +
                    " \"foot_size\" will be labeled as \"Foot size\", \"REALLY_custom.user-Attribute\" will be translated " +
                    "to \"Really custom user attribute\", etc. By default, set to true. If User attribute form label " +
                    "is configured, label is taken form configuration and generation is skipped.")
            .add()

            .property()
            .name(CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
            .label("Clear user on validation fail")
            .defaultValue("true") // only string value is accepted
            .helpText("If enabled, user is not stored in session context in case username and password were valid but user attribute was not.")
            .add()

            .property()
            .name(LOGIN_FORM_ATTRIBUTE_LABEL)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute form label")
            .helpText("Message which will be displayed as user attribute input label. If value is a valid message key, then proper translation will be used.")
            .add()

            .property()
            .name(LOGIN_FORM_ERROR_MESSAGE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("Validation error message")
            .helpText("Message which will be displayed as user attribute validation error. If value is a valid message key, then proper translation will be used.")
            .add()

            .build();

    static String configPropertyOf(final AuthenticationFlowContext context, final String configPropertyName) {
        return Optional.ofNullable(System.getenv(configPropertyName.toUpperCase()))
                .orElse(context.getAuthenticatorConfig().getConfig().get(configPropertyName));
    }

    static boolean isGenerateLabelEnabled(final AuthenticationFlowContext context) {
        return Boolean.parseBoolean(configPropertyOf(context, LOGIN_FORM_GENERATE_LABEL));
    }

    static boolean isClearUserOnFailedAttributeValidationEnabled(final AuthenticationFlowContext context) {
        return Boolean.parseBoolean(configPropertyOf(context, CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL));
    }
}