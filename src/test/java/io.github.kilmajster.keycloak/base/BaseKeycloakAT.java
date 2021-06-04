package io.github.kilmajster.keycloak.base;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseKeycloakAT {

    protected static final Logger log = LoggerFactory.getLogger(BaseKeycloakAT.class);

    protected static final String KEYCLOAK_DEV_DOCKER_IMAGE = "kilmajster/keycloak-with-authenticator:test";
    protected static final String KEYCLOAK_LOCAL_URL_PREFIX = "http://localhost:";

    protected String getKeycloakBaseUrl(final KeycloakContainer keycloakContainer) {
        return KEYCLOAK_LOCAL_URL_PREFIX + keycloakContainer.getFirstMappedPort();
    }
}