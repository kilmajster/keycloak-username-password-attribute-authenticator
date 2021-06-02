package io.github.kilmajster.keycloak.base;

import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public abstract class BaseKeycloakInDockerAT {

    protected static final Logger log = LoggerFactory.getLogger(BaseKeycloakInDockerAT.class);

    protected static final String KEYCLOAK_DEV_DOCKER_IMAGE = "kilmajster/keycloak-with-authenticator:test";
    protected static final String KEYCLOAK_NETWORK_ALIAS = "keycloak-host";
    protected static final int KEYCLOAK_DEFAULT_PORT = 8080;
    protected static final String KEYCLOAK_TEST_URL = "http://" + KEYCLOAK_NETWORK_ALIAS + ":" + KEYCLOAK_DEFAULT_PORT;

    @Rule
    public Network testNetwork = Network.newNetwork();
}