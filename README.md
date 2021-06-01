# keycloak-username-password-attribute-authenticator

## Description
Keycloak default login form with user attribute validation.

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

