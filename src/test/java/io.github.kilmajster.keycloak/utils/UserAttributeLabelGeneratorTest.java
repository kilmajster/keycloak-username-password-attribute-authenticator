package io.github.kilmajster.keycloak.utils;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UserAttributeLabelGeneratorTest {

    @Test
    public void shouldPrettifyString() {
        final String attribute = "TEST_ATTRIBUTE-NAME.bLah";

        final String label = UserAttributeLabelGenerator.generateLabel(attribute);

        assertThat(label).isEqualTo("Test attribute name blah");
    }
}