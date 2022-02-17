# Keycloak username password attribute authenticator
[![automation tests](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/kilmajster/keycloak-username-password-attribute-authenticator?label=docker%20hub)
![Docker Pulls](https://img.shields.io/docker/pulls/kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)
[![compatible with Keycloak - 16.1.1](https://img.shields.io/badge/compatible_with_Keycloak-16.1.1-2ea44f)](https://)
## Description
Keycloak default login form with additional user attribute validation. Example:

<p align="center">
  <img alt="Login form preview" src="/.github/img/foot-size-form.png" width="48%">
&nbsp; &nbsp;
  <img alt="Form error message preview" src="/.github/img/foot-size-form-error.png" width="48%">
</p>

## Usage
To use this authenticator, it should be bundled together with Keycloak, here are two ways how to do that:

### Deploying jar file
To deploy custom Keycloak extension it needs to be placed in `{$KEYCLOAK_PATH}/standalone/deployments/`.
Latest authenticator jar file can be downloaded from
[Github Releases](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/releases/latest) page or
[Maven Central Repository](https://mvnrepository.com/artifact/io.github.kilmajster/keycloak-username-password-attribute-authenticator/latest).

### Using Docker init container
If you want to use this authenticator in cloud environment, here is ready [init container](https://hub.docker.com/r/kilmajster/keycloak-username-password-attribute-authenticator).
Jar file is placed in `/opt/jboss/keycloak/standalone/deployments`, so same location as target one.
According to official Keycloak [example](https://github.com/codecentric/helm-charts/blob/master/charts/keycloak/README.md#providing-a-custom-theme),
Helm chart could look like following:
```yaml
extraInitContainers: |
  - name: attribute-authenticator-provider
    image: kilmajster/keycloak-username-password-attribute-authenticator:latest
    imagePullPolicy: IfNotPresent
    command:
      - sh
    args:
      - -c
      - |
        echo "Copying attribute authenticator..."
        cp -R /opt/jboss/keycloak/standalone/deployments/*.jar /attribute-authenticator
    volumeMounts:
      - name: attribute-authenticator
        mountPath: /attribute-authenticator

extraVolumeMounts: |
  - name: attribute-authenticator
    mountPath: /opt/jboss/keycloak/standalone/deployments

extraVolumes: |
  - name: attribute-authenticator
    emptyDir: {}
``` 

## Configuration
Following steps shows how to create authentication flow that uses authenticator with user attribute validation.
1. In Keycloak admin console, go to _Authentication_ section, select authentication type of _Browser_ and click _Copy_.
2. Set name for new authentication flow eg. `Browser with user attribute` and click _Ok_.
3. In newly created authentication flow remove _Username Password Form_ execution.
4. On _Browser With User Attribute Forms_ level, click _Actions_ > _Add execution_ and select provider of type
   _Username Password Attribute Form_, then save.
<p align="center">
    <img src="/.github/img/new-authenticator-execution.png" alt="New authentication execution">
</p>

5. Then move _Username Password Attribute Form_ on a previous position of _Username Password Form_,
   so in the end authentication flow should look like following:
<p align="center">
    <img src="/.github/img/foot-size-execution-config-tooltip.png" alt="Form config tooltip">
</p>

6. On _Username Password Attribute Form_ level, click _Actions_ > _Config_.
<p align="center">
    <img src="/.github/img/foot-size-form-config.png" alt="Authenticator configuration">
</p>

### Minimal configuration
 - ##### `User attribute`
   Attribute used to validate login form.
### Advanced configuration
 - ##### `Generate label` (default true)
   If enabled, label for login form will be generated based on attribute name, so attribute with name:
   - `foot_size` will be labeled as _Foot size_
   - `REALLY_custom.user-Attribute` will be translated to _Really custom user attribute_, etc.
   
   By default, set to `true`. If `User attribute form label`
   is configured, label is taken form configuration and generation is skipped.
 - ##### `Clear user on validation fail` (default true)
   If enabled, user is not stored in session context in case username and password were valid but user attribute was not.
 - ##### `User attribute form label`
   Message which will be displayed as user attribute input label. If value is a valid message key, then proper translation will be used.
 - ##### `Validation error message`
   Message which will be displayed as user attribute validation error. If value is a valid message key, then proper translation will be used.

#### Configuration via environment variables
Configuration could be also provided as environment variables.
If such config exists, then configuration from Keycloak admin UI is ignored. Available properties:
- LOGIN_FORM_GENERATE_LABEL
- LOGIN_FORM_ATTRIBUTE_LABEL
- LOGIN_FORM_ERROR_MESSAGE
- CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL

### Theme configuration
Theme configuration is handled in clients section, in following example Keycloak default `account-console` client will be used.

#### Using bundled default Keycloak theme
In Keycloak admin panel, go to _Clients_ and select client you want to authenticate with user attribute form. As _Login Theme_ set `base-with-attribute`
and in _Authentication Flow Overrides_ for _Browser Flow_, choose authentication that contain previously configured login form,
so for example `Browser with user attribute`, like below:
<p align="center">
    <img src="/.github/img/example-client-config.png" alt="Example client configuration">
</p>

#### Extending own theme
If you have your own theme, then in `.your-theme/login/login.ftl` add following below `<div>` responsible for a password stuff or anywhere you want.
How it was done with _Keycloak base_ theme, you can check [here](/src/main/resources/theme/base-with-attribute/login/login.ftl).
```html
...
<div class="${properties.kcFormGroupClass!}">
    <label for="password" class="${properties.kcLabelClass!}">${msg("password")}</label>

    <input tabindex="2" id="password" class="${properties.kcInputClass!}" name="password" type="password" autocomplete="off"
           aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
    />
</div>

<!-- keycloak-user-attribute-authenticator custom code block start -->
<div class="${properties.kcFormGroupClass!}">
    <label for="login_form_user_attribute" class="${properties.kcLabelClass!}">
        <#if login_form_attribute_label??>
            ${msg(login_form_attribute_label)}
        <#else>
            ${msg("login_form_attribute_label_default")}
        </#if>
    </label>

    <input tabindex="3" id="login_form_user_attribute" class="${properties.kcInputClass!}"
           name="login_form_user_attribute" type="text" autocomplete="off"
           aria-invalid="<#if messagesPerField.existsError('login_form_user_attribute')>true</#if>"
    />
</div>
<!-- keycloak-user-attribute-authenticator custom code block end -->

<div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
...        
```

-----------------
### Testing & development
#### Build the project
```shell
$ mvn package
```

#### Run Keycloak with authenticator in docker-compose
After building a project, do following to start Keycloak with bundled authenticator jar and dummy configuration ([`dev-realm.json`](src/test/resources/dev-realm.json)).
```shell
$ docker-compose up --build
```
Open browser and go to http://localhost:8081/auth/realms/dev-realm/account
use _Username or email_ = `test`, _Password_ = `test` and _Foot size_ = `46` to login.

##### Debug in docker with IntelliJ
`.github/debug-in-docker.run.xml`

#### Automation tests
##### Build test docker image
```shell
$ docker-compose build
```

##### Running tests with chrome
```shell
$ mvn test -P automation-tests
```

##### Running tests in docker
```shell
$ mvn test -P automation-tests -D selenide.headless=true
```