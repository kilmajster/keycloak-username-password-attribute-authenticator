FROM busybox

ARG VERSION

COPY target/keycloak-username-password-attribute-authenticator-${VERSION}.jar /opt/jboss/keycloak/standalone/deployments/