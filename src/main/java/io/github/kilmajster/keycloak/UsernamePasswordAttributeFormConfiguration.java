package io.github.kilmajster.keycloak;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;
import java.util.Optional;

public interface UsernamePasswordAttributeFormConfiguration {

    String USER_ATTRIBUTE = "user_attribute";
    String GENERATE_LABEL = "generate_label";
    String USER_ATTRIBUTE_LABEL = "user_attribute_label";
    String USER_ATTRIBUTE_ERROR_MESSAGE = "user_attribute_error_message";

    List<ProviderConfigProperty> PROPS = ProviderConfigurationBuilder.create()

            .property()
            .name(USER_ATTRIBUTE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute")
            .helpText("Attribute used to validate login form.")
            .add()

            .property()
            .name(GENERATE_LABEL)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
            .label("Generate label")
            .defaultValue("true") // only string value is accepted
            .helpText("If enabled, label for login form will be generated based on attribute name, so attribute with name:" +
                    " \"favorite_number\" will be labeled as \"Favorite number\", \"REALLY_custom.user-Attribute\" will be translated " +
                    "to \"Really custom user attribute\", etc. By default, set to true. If User attribute form label " +
                    "is configured, label is taken form configuration and generation is skipped.")
            .add()

            .property()
            .name(USER_ATTRIBUTE_LABEL)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute form label")
            .helpText("Message which will be displayed as user attribute input label. If value is a valid message key, then proper translation will be used.")
            .add()

            .property()
            .name(USER_ATTRIBUTE_ERROR_MESSAGE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("Invalid user attribute error message")
            .helpText("Message which will be displayed for invalid user attribute error message. If value is a valid message key, then proper translation will be used.")
            .add()

            .build();

    static String configPropertyOf(final AuthenticationFlowContext context, final String configPropertyName) {
        return Optional.ofNullable(System.getenv(configPropertyName.toUpperCase()))
                .orElse(context.getAuthenticatorConfig().getConfig().get(configPropertyName));
    }

    static boolean isGenerateLabelEnabled(final AuthenticationFlowContext context) {
        return Boolean.parseBoolean(configPropertyOf(context, GENERATE_LABEL));
    }
}