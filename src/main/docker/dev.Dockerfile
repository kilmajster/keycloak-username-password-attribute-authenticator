FROM quay.io/keycloak/keycloak:latest

ARG VERSION=SNAPSHOT

ADD target/keycloak-username-password-attribute-authenticator-${VERSION}.jar /opt/jboss/keycloak/standalone/deployments