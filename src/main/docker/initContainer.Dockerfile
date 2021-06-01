FROM busybox

COPY target/keycloak-username-password-attribute-authenticator-*.jar /opt/jboss/keycloak/standalone/deployments/