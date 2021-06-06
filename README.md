# Keycloak username password attribute authenticator
[![automation tests](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/kilmajster/keycloak-username-password-attribute-authenticator?label=docker%20hub)
![Docker Pulls](https://img.shields.io/docker/pulls/kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)

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
### Authenticator config
#### config via Keycloak UI / API
- login_form_user_attribute
- login_form_generate_label
- login_form_attribute_label

#### config via env variables
- LOGIN_FORM_USER_ATTRIBUTE
- LOGIN_FORM_GENERATE_LABEL
- LOGIN_FORM_ATTRIBUTE_LABEL

### Theme config
#### Using bundled default keycloak theme
#### Extending own theme

<div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
...        
```

-----------------
### Development
#### Build the project
```shell
$ mvn package
```

#### Run Keycloak with authenticator in Docker
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