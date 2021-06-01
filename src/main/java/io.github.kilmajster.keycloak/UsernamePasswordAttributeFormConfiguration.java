package io.github.kilmajster.keycloak;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;
import java.util.Optional;

public interface UsernamePasswordAttributeFormConfiguration {

    String USER_ATTRIBUTE = "login_form_user_attribute";
    String GENERATE_FORM_LABEL = "login_form_generate_label";
    String USER_ATTRIBUTE_LABEL = "login_form_attribute_label";

    List<ProviderConfigProperty> PROPS = ProviderConfigurationBuilder.create()

            .property()
            .name(USER_ATTRIBUTE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute key name")
            .helpText("TODO")
            .add()

            .property()
            .name(GENERATE_FORM_LABEL)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
            .label("Generate label")
            .defaultValue(true)
            .helpText("TODO")
            .add()

            .property()
            .name(USER_ATTRIBUTE_LABEL)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("User attribute form label")
            .helpText("TODO")
            .add()

            .build();

    static String configPropertyOf(final AuthenticationFlowContext context, final String configPropertyName) {
        return Optional.ofNullable(System.getenv(configPropertyName.toUpperCase()))
                .orElse(context.getAuthenticatorConfig().getConfig().get(configPropertyName));
    }

    static boolean isGeneratePropertyLabelEnabled(final AuthenticationFlowContext context) {
        return Boolean.parseBoolean(configPropertyOf(context, GENERATE_FORM_LABEL));
    }
}