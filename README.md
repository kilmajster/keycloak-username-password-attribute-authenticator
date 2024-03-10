# Keycloak username password attribute authenticator
[![automation tests](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)

| <img src="https://img.shields.io/badge/compatible_with_Keycloak-16.1.1-orange" alt="compatible with Keycloak - 16.1.1"> | [`keycloak-username-password-attribute-authenticator:0.3.0`](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/tree/0.3.0) |
|-------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://img.shields.io/badge/compatible_with_Keycloak-24.0.1-blue" alt="compatible with Keycloak - 24.0.1">   | [`keycloak-username-password-attribute-authenticator:1.0.0`](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/tree/main)  |

## Description
Keycloak default login form with additional user attribute validation. Example:

<p align="center">
  <img alt="Login form preview" src="/.github/docs/login-form.png" width="48%">
&nbsp; &nbsp;
  <img alt="Form error message preview" src="/.github/docs/login-form-error.png" width="48%">
</p>

## Usage
To use this authenticator, it should be bundled together with Keycloak, here are two ways how to do that:

### Deploying jar 
 TODO

## Authentication configuration
Following steps shows how to create authentication flow that uses authenticator with user attribute validation.
1. In Keycloak admin console, go to _Authentication_ section, select authentication type of _Browser_ and click 
_Duplicate_ action.
2. Set name for new authentication flow eg. `Browser with user attribute` and click _Ok_.
3. In newly created authentication flow remove _Username Password Form_ execution.
4. On _Browser With User Attribute Forms_ level, click _Actions_ > _Add execution_ and select provider of type
   _Username Password Attribute Form_, set _Requirement_ to `required`, then save.
5. Then move _Username Password Attribute Form_ on a previous position of _Username Password Form_,
   so in the end authentication flow should look like following:
    <p align="center">
        <img src="/.github/docs/config-authentication.png" alt="New authentication execution">
    </p>
6. On _Username Password Attribute Form_ level, click _Actions_ > _Settings_.
    <p align="center">
        <img src="/.github/docs/config-execution.png" alt="Authenticator configuration">
    </p>

### Minimal configuration
- ##### `User attribute`
  Attribute used to validate login form.
### Advanced configuration
- ##### `Generate label` (default true)
  If enabled, label for login form will be generated based on attribute name, so attribute with name:
    - `favorite_number` will be labeled as _Favorite number_
    - `REALLY_custom.user-Attribute` will be translated to _Really custom user attribute_, etc.
  By default, set to `true`. If `User attribute form label`
  is configured, label is taken form configuration and generation is skipped.
- ##### `User attribute form label`
  Message which will be displayed as user attribute input label. If value is a valid message key, then proper translation will be used.
- ##### `Invalid user attribute error message`
  Message which will be displayed as user attribute validation error. If value is a valid message key, then proper translation will be used.

## Theme configuration
Theme configuration is handled in clients section, in following example Keycloak default `account-console` client will be used.

### Using bundled default Keycloak theme
In Keycloak admin panel, go to _Clients_ and select client you want to authenticate with user attribute form. As _Login Theme_ set `base-with-attribute`
<p align="center">
    <img src="/.github/docs/config-client-login-theme.png" alt="Example client configuration">
</p>
Then in advance section > _Authentication Flow Overrides_ for _Browser Flow_, choose authentication that contain previously configured login form,
so for example `Browser with user attribute`.
<p align="center">
    <img src="/.github/docs/config-auth-flow-override.png" alt="Example client configuration">
</p>


### Extending own theme
If you have your own theme, then in `.your-theme/login/login.ftl` add following below `<div>` responsible for a password stuff or anywhere you want.
How it was done with _Keycloak base_ theme, you can check [here](/src/main/resources/theme/base-with-attribute/login/login.ftl).
```html
    <#if usernameHidden?? && messagesPerField.existsError('username','password')>
        <span id="input-error" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
        </span>
    </#if>

</div>

<!-- attribute input block start-->
<div class="${properties.kcFormGroupClass!}">
    <label for="user_attribute" class="${properties.kcLabelClass!}"><#if user_attribute_label??>${msg(user_attribute_label)}<#else>${msg("defaultUserAttributeLabel")}</#if></label>
    <div class="${properties.kcInputGroup!}">
        <input tabindex="3" id="user_attribute" class="${properties.kcInputClass!}" name="user_attribute" type="password" autocomplete="current-user-attribute"
            aria-invalid="<#if messagesPerField.existsError('username','password','user_attribute')>true</#if>"
        />
        <button class="${properties.kcFormPasswordVisibilityButtonClass!}" type="button" aria-label="${msg("showUserAttribute")}"
            aria-controls="user_attribute" data-password-toggle tabindex="4"
            data-icon-show="${properties.kcFormPasswordVisibilityIconShow!}" data-icon-hide="${properties.kcFormPasswordVisibilityIconHide!}"
            data-label-show="${msg('showUserAttribute')}" data-label-hide="${msg('hideUserAttribute')}">
            <i class="${properties.kcFormPasswordVisibilityIconShow!}" aria-hidden="true"></i>
        </button>
    </div>
    <#if usernameHidden?? && messagesPerField.existsError('username','password', 'user_attribute')>
    <span id="input-error" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
        ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
    </span>
    </#if>
</div>
<!-- attribute input block end-->

<div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
    <div id="kc-form-options">
        <#if realm.rememberMe && !usernameHidden??>
            <div class="checkbox">
```

### Testing & development
#### Build the project
```shell
$ mvn package
```
#### Run Keycloak with authenticator in docker compose
After building a project, do following to start Keycloak with bundled authenticator jar and dummy configuration ([`dev-realm.json`](dev-realm.json)).
```shell
$ docker compose up
```
Open browser and go to http://localhost:8081/auth/realms/dev-realm/account
use _Username or email_ = `test`, _Password_ = `test` and _Favorite number_ = `46` to login.
