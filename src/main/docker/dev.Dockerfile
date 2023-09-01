FROM quay.io/keycloak/keycloak:22.0.1

ARG VERSION=SNAPSHOT

ADD target/keycloak-username-password-attribute-authenticator-${VERSION}.jar /opt/keycloak/providers
ADD src/test/resources/dev-realm.json /opt/keycloak/data/import/dev-realm.json

ENV KC_PROXY=edge
ENV KC_HTTP_PORT=8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "--verbose", "start-dev", "--import-realm"]