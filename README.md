# Keycloak username password attribute 
[![automation tests](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/kilmajster/keycloak-username-password-attribute-authenticator?label=docker%20hub)
![Docker Pulls](https://img.shields.io/docker/pulls/kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)

## Description
Keycloak default login form with user attribute validation.

## How 2 use
To use this authenticator, it should be bundled together with Keycloak, here are two ways how to do that:
### using jar



### using docker init container
If you want to use this authenticator in some cloud envirenement, here is ready init container. Jar file is placed in `/opt/jboss/keycloak/standalone/deployments`, 
so same location as target one. Possible 
```
kilmajster/keycloak-username-password-attribute-authenticator:latest
```
#### example helm chart snippet

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
$ mvn test -P automation-tests -D headless=true
```

