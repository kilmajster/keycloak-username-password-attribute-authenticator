FROM jboss/keycloak:16.1.1

ARG VERSION=SNAPSHOT

ADD target/keycloak-username-password-attribute-authenticator-${VERSION}.jar /opt/jboss/keycloak/standalone/deployments