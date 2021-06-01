FROM quay.io/keycloak/keycloak:latest

ADD target/keycloak-username-password-attribute-authenticator-*.jar /opt/jboss/keycloak/standalone/deployments