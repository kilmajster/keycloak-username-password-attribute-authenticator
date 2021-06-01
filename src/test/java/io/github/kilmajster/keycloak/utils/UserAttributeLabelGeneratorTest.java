package io.github.kilmajster.keycloak.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAttributeLabelGeneratorTest {

    @Test
    void shouldPrettifyString() {
        final String attribute = "TEST_ATTRIBUTE-NAME";

        final String label = UserAttributeLabelGenerator.from(attribute);

        assertThat(label).isEqualTo("Test attribute name");
    }
}