package io.github.kilmajster.keycloak;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.services.ServicesLogger;

import static io.github.kilmajster.keycloak.UsernamePasswordAttributeFormConfiguration.*;
import static org.keycloak.services.validation.Validation.FIELD_PASSWORD;

public class UsernamePasswordAttributeForm extends UsernamePasswordForm implements Authenticator {

    protected static ServicesLogger log = ServicesLogger.LOGGER;

    @Override
    protected Response challenge(AuthenticationFlowContext context, String error, String field) {
        setUserAttributeFormLabel(context);
        setUserAttributeFormErrorMessage(context);

        return super.challenge(context, null, null);
    }

    @Override
    protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        setUserAttributeFormLabel(context);

        return super.challenge(context, formData);
    }

    @Override
    protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        return super.validateForm(context, formData) && isUserAttributeValid(context, formData.getFirst(USER_ATTRIBUTE));
    }

    private void setUserAttributeFormLabel(AuthenticationFlowContext context) {
        String userAttributeLabel = configPropertyOf(context, USER_ATTRIBUTE_LABEL);

        // label
        if (userAttributeLabel != null) {
            context.form().setAttribute(USER_ATTRIBUTE_LABEL, userAttributeLabel);
        } else {
            String userAttributeName = configPropertyOf(context, USER_ATTRIBUTE);
            if (userAttributeName != null && !userAttributeName.isEmpty()) {
                context.form().setAttribute(
                        USER_ATTRIBUTE_LABEL,
                        isGenerateLabelEnabled(context)
                                ? generateLabel(userAttributeName)
                                : userAttributeName
                );
            } else {
                log.warn("Configuration of keycloak-user-attribute-authenticator is incomplete! " +
                        "At least user_attribute property needs to be set!");
            }
        }
    }

    private void setUserAttributeFormErrorMessage(AuthenticationFlowContext context) {
        String userAttributeName = configPropertyOf(context, USER_ATTRIBUTE);
        String userAttributeErrorMessage = configPropertyOf(context, USER_ATTRIBUTE_ERROR_MESSAGE);

        if (userAttributeErrorMessage != null) {
            context.form().addError(
                    new FormMessage(FIELD_PASSWORD, userAttributeErrorMessage, userAttributeName)
            );
        } else {
            if (userAttributeName != null && !userAttributeName.isEmpty()) {
                context.form().addError(
                        new FormMessage(
                                FIELD_PASSWORD,
                                "invalidPasswordOrAttributeMessage",
                                isGenerateLabelEnabled(context)
                                        ? generateLabel(userAttributeName)
                                        : userAttributeName
                        )
                );
            } else {
                log.warn("Configuration of keycloak-user-attribute-authenticator is incomplete! " +
                        "At least user_attribute property needs to be set!");
            }
        }
    }

    private boolean isUserAttributeValid(AuthenticationFlowContext context, String providedAttribute) {
        String attributeName = context.getAuthenticatorConfig().getConfig().get(USER_ATTRIBUTE);
        UserModel user = context.getUser();
        boolean attributeValid = user != null
                && user.getAttributeStream(attributeName).anyMatch(attr -> attr.equals(providedAttribute));

        return attributeValid || badAttributeHandler(context, user);
    }

    private boolean badAttributeHandler(AuthenticationFlowContext context, UserModel user) {
        context.getEvent().user(user);
        context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
        context.getEvent().detail(Details.REASON, "Invalid user attribute was provided");

        if (isUserAlreadySetBeforeUsernamePasswordAuth(context)) {
            LoginFormsProvider form = context.form();
            form.setAttribute(LoginFormsProvider.USERNAME_HIDDEN, true);
            form.setAttribute(LoginFormsProvider.REGISTRATION_DISABLED, true);
        }

        Response challengeResponse = challenge(context, getDefaultChallengeMessage(context), FIELD_PASSWORD);
        context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challengeResponse);
        context.clearUser();

        return false;
    }

    private String generateLabel(final String attributeName) {
        final String lowercaseWithSpaces = attributeName
                .toLowerCase()
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ");

        return capitalize(lowercaseWithSpaces);
    }

    private String capitalize(final String lowercaseWithSpaces) {
        return lowercaseWithSpaces.substring(0, 1).toUpperCase() + lowercaseWithSpaces.substring(1).toLowerCase();
    }
}