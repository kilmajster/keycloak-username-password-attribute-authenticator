package io.github.kilmajster.keycloak;

import org.keycloak.Config;
import org.keycloak.OAuth2Constants;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.DisplayTypeAuthenticatorFactory;
import org.keycloak.authentication.authenticators.console.ConsoleUsernamePasswordAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public class UsernamePasswordAttributeFormFactory implements AuthenticatorFactory, DisplayTypeAuthenticatorFactory {

    public static final String PROVIDER_ID = "auth-username-password-attr-form";
    public static final UsernamePasswordAttributeForm SINGLETON = new UsernamePasswordAttributeForm();

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return UsernamePasswordAttributeFormConfiguration.PROPS;
    }

    @Override
    public Authenticator createDisplay(KeycloakSession session, String displayType) {
        if (displayType == null) return SINGLETON;
        if (!OAuth2Constants.DISPLAY_CONSOLE.equalsIgnoreCase(displayType)) return null;
        return ConsoleUsernamePasswordAuthenticator.SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return PasswordCredentialModel.TYPE;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Username Password Attribute Form";
    }

    @Override
    public String getHelpText() {
        return "Validates a username, password and selected user attribute from login form.";
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }
}