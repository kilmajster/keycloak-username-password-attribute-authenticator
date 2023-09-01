package io.github.kilmajster.keycloak;

import io.github.kilmajster.keycloak.ui.UserAttributeLabelGenerator;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.ModelDuplicateException;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.messages.Messages;
import org.keycloak.services.validation.Validation;


import static io.github.kilmajster.keycloak.UsernamePasswordAttributeFormConfiguration.*;

public class UsernamePasswordAttributeForm extends UsernamePasswordForm implements Authenticator {

    protected static ServicesLogger log = ServicesLogger.LOGGER;

    @Override
    protected Response challenge(AuthenticationFlowContext context, String error, String field) {
        LoginFormsProvider form = context.form().setExecution(context.getExecution().getId());
        if (error != null) {
            if (field != null) {
                form.addError(new FormMessage(field, error));
            } else {
                form.setError(error, new Object[0]);
            }
        }

        configureUserAttributeLabel(context);

        return createLoginForm(form);
    }

    @Override
    protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        LoginFormsProvider forms = context.form();
        if (formData.size() > 0) {
            forms.setFormData(formData);
        }

        configureUserAttributeLabel(context);

        return forms.createLoginUsernamePassword();
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        String loginHint = context.getAuthenticationSession().getClientNote("login_hint");
        String rememberMeUsername = AuthenticationManager.getRememberMeUsername(context.getRealm(), context.getHttpRequest().getHttpHeaders());
        if (loginHint != null || rememberMeUsername != null) {
            if (loginHint != null) {
                formData.add("username", loginHint);
            } else {
                formData.add("username", rememberMeUsername);
                formData.add("rememberMe", "on");
            }
        }

        Response challengeResponse = challenge(context, formData);
        context.challenge(challengeResponse);
    }

    private void configureUserAttributeLabel(AuthenticationFlowContext context) {
        final String userAttributeLabel = configPropertyOf(context, LOGIN_FORM_ATTRIBUTE_LABEL);
        if (userAttributeLabel != null) {
            context.form().setAttribute(LOGIN_FORM_ATTRIBUTE_LABEL, userAttributeLabel);
        } else {
            final String userAttributeName = configPropertyOf(context, LOGIN_FORM_USER_ATTRIBUTE);
            if (userAttributeName != null && !userAttributeName.isEmpty()) {
                context.form().setAttribute(LOGIN_FORM_ATTRIBUTE_LABEL,
                        isGenerateLabelEnabled(context)
                                ? UserAttributeLabelGenerator.generateLabel(userAttributeName)
                                : userAttributeName);
            } else {
                log.warn("Configuration of keycloak-user-attribute-authenticator is incomplete! " +
                        "At least user_attribute property needs to be set!");
            }
        }
    }

    @Override
    protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        context.clearUser();
        UserModel user = getUser(context, formData);
        return user != null
                && validatePassword(context, user, formData, true) && validateUser(context, user, formData)
                && validateUserAttribute(context, user, formData);
    }

    private boolean validateUserAttribute(AuthenticationFlowContext context, UserModel user, MultivaluedMap<String, String> formData) {
        final String providedAttribute = formData.getFirst(LOGIN_FORM_USER_ATTRIBUTE);
        if (providedAttribute == null || providedAttribute.isEmpty()) {
            return invalidUserAttributeHandler(context, user, true);
        }

        if (isProvidedAttributeValid(context, user, providedAttribute)) {
            return true;
        } else {
            return invalidUserAttributeHandler(context, user, false);
        }
    }

    private boolean isProvidedAttributeValid(AuthenticationFlowContext context, UserModel user, String providedUserAttribute) {
        String userAttributeName = context.getAuthenticatorConfig().getConfig().get(LOGIN_FORM_USER_ATTRIBUTE);
        return user.getAttributeStream(userAttributeName)
                .anyMatch(attr -> attr.equals(providedUserAttribute));
    }

    // Set up AuthenticationFlowContext error.
    private boolean invalidUserAttributeHandler(AuthenticationFlowContext context, UserModel user, boolean isAttributeEmpty) {
        context.getEvent().user(user);
        context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);

        String errorText;
        final String configuredErrorMessage = configPropertyOf(context, LOGIN_FORM_ERROR_MESSAGE);
        if (configuredErrorMessage != null && !configuredErrorMessage.isBlank()) {
            // error text directly from error label propertyModelException
            errorText = configuredErrorMessage;
        } else {
            final String userAttributeLabel = configPropertyOf(context, LOGIN_FORM_ATTRIBUTE_LABEL);
            if (userAttributeLabel != null && !userAttributeLabel.isBlank()) {
                // get message from message.properties in case USER_ATTRIBUTE_LABEL is a message key
                final String message = context.form().getMessage(userAttributeLabel);
                // generating error message based on provided user attribute label
                errorText = UserAttributeLabelGenerator.generateErrorText(message != null ? message : userAttributeLabel);
            } else {
                // user attribute label not provided so generating text based on attribute name
                errorText = isGenerateLabelEnabled(context) // generate pretty error if property is not disabled
                        ? UserAttributeLabelGenerator.generateErrorText(configPropertyOf(context, LOGIN_FORM_USER_ATTRIBUTE))
                        : "Invalid ".concat(configPropertyOf(context, LOGIN_FORM_USER_ATTRIBUTE)); // use raw attribute name
            }
        }

        if (isClearUserOnFailedAttributeValidationEnabled(context)) {
            context.clearUser();
        }

        Response challengeResponse = challenge(context, errorText, LOGIN_FORM_USER_ATTRIBUTE);

        if (isAttributeEmpty) {
            context.forceChallenge(challengeResponse);
        } else {
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challengeResponse);
        }

        context.clearUser();

        return false;
    }


    private UserModel getUser(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        String username = formData.getFirst(AuthenticationManager.FORM_USERNAME);
        if (username == null) {
            context.getEvent().error(Errors.USER_NOT_FOUND);
            Response challengeResponse = challenge(context, getDefaultChallengeMessage(context), Validation.FIELD_USERNAME);
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return null;
        }

        // remove leading and trailing whitespace
        username = username.trim();

        context.getEvent().detail(Details.USERNAME, username);
        context.getAuthenticationSession().setAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME, username);

        UserModel user = null;
        try {
            user = KeycloakModelUtils.findUserByNameOrEmail(context.getSession(), context.getRealm(), username);
        } catch (ModelDuplicateException mde) {
            ServicesLogger.LOGGER.modelDuplicateException(mde);

            // Could happen during federation import
            if (mde.getDuplicateFieldName() != null && mde.getDuplicateFieldName().equals(UserModel.EMAIL)) {
                setDuplicateUserChallenge(context, Errors.EMAIL_IN_USE, Messages.EMAIL_EXISTS, AuthenticationFlowError.INVALID_USER);
            } else {
                setDuplicateUserChallenge(context, Errors.USERNAME_IN_USE, Messages.USERNAME_EXISTS, AuthenticationFlowError.INVALID_USER);
            }
            return user;
        }

        testInvalidUser(context, user);
        return user;
    }

    private boolean validateUser(AuthenticationFlowContext context, UserModel user, MultivaluedMap<String, String> inputData) {
        if (!enabledUser(context, user)) {
            return false;
        }
        String rememberMe = inputData.getFirst("rememberMe");
        boolean remember = rememberMe != null && rememberMe.equalsIgnoreCase("on");
        if (remember) {
            context.getAuthenticationSession().setAuthNote(Details.REMEMBER_ME, "true");
            context.getEvent().detail(Details.REMEMBER_ME, "true");
        } else {
            context.getAuthenticationSession().removeAuthNote(Details.REMEMBER_ME);
        }
        context.setUser(user);
        return true;
    }
}