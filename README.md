# Keycloak username password attribute authenticator
[![automation tests](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/kilmajster/keycloak-username-password-attribute-authenticator?label=docker%20hub)
![Docker Pulls](https://img.shields.io/docker/pulls/kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)

## Description
Keycloak default login form with additional user attribute validation. Example:

<p align="center">
  <img alt="Login form preview" src="/.github/img/foot-size-form.png" width="47%">
&nbsp; &nbsp;
  <img alt="Form error message preview" src="/.github/img/foot-size-form-error.png" width="47%">
</p>

## Usage
To use this authenticator, it should be bundled together with Keycloak, here are two ways how to do that:

### Deploying jar file
To deploy custom Keycloak extension it needs to be placed in `{$KEYCLOAK_PATH}/standalone/deployments/`.
Latest authenticator jar file can be downloaded from 
[Github Releases](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/releases/latest) or 
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
### Authentication configuration
<p align="center">
    <img src="/.github/img/new-authenticator-execution.png" alt="New authentication execution">
</p>

<p align="center">
    <img src="/.github/img/foot-size-execution-config-tooltip.png" alt="Form config tooltip">
</p>

#### Minimal configuration
- login_form_user_attribute

<p align="center">
    <img src="/.github/img/shoe-size-form-config.png" alt="Authenticator configuration">
</p>

#### Advanced configuration
 - login_form_generate_label
 - login_form_attribute_label
 - login_form_error_message
 - clear_user_on_attribute_validation_fail
##### config via Keycloak API
TODO
##### Configuration via environment variables
- LOGIN_FORM_GENERATE_LABEL
- LOGIN_FORM_ATTRIBUTE_LABEL
- LOGIN_FORM_ERROR_MESSAGE
- CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL

### Theme configuration
#### Using bundled default keycloak theme
 - choose theme `base-with-attribute`
 - override authentication flow to `Browser with user attribute`

#### Extending own theme
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

-------------------------------------
### Development
#### build the project
```shell
$ mvn package
```
#### run with docker-compose
```shell
$ docker-compose up --build
```
http://localhost:8081/auth/realms/dev-realm/account
##### debug in docker with IntelliJ
`.github/debug-in-docker.run.xml`

#### automation tests
##### build test docker image
```shell
$ docker-compose build
```
##### running tests with chrome
```shell
$ mvn test -P automation-tests
```
##### running tests in docker
```shell
$ mvn test -P automation-tests -D selenide.headless=true
```