# Keycloak username password attribute authenticator
[![CI with Selenide](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation_tests.yml/badge.svg)](https://github.com/kilmajster/keycloak-username-password-attribute-authenticator/actions/workflows/automation_tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.kilmajster/keycloak-username-password-attribute-authenticator)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/kilmajster/keycloak-username-password-attribute-authenticator?label=docker%20hub)
![Docker Pulls](https://img.shields.io/docker/pulls/kilmajster/keycloak-username-password-attribute-authenticator)
![GitHub](https://img.shields.io/github/license/kilmajster/keycloak-username-password-attribute-authenticator)

## Description
Keycloak default login form with user attribute validation.

## Using
### using jar
### using docker init container

## Configuration
### Authenticator config
#### config via Keycloak UI
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

## docker
### example in docker-compose
```shell
$ docker-compose up --build --force-recreate
```
 http://localhost:8081/auth/realms/dev-realm/account


### init container
```
kilmajster/keycloak-username-password-attribute-authenticator:latest
```

