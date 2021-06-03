FROM quay.io/keycloak/keycloak:latest

ADD target/keycloak-username-password-attribute-authenticator-SNAPSHOT.jar /opt/jboss/keycloak/standalone/deployments