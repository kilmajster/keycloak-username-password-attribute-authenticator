package io.github.kilmajster.keycloak.utils;

public class UserAttributeLabelGenerator {

    public static String from(final String attributeName) {
        final String lowercaseWithSpaces = attributeName
                .toLowerCase()
                .replace("_", " ")
                .replace("-", " ");

        return capitalizeFirstChar(lowercaseWithSpaces);
    }

    private static String capitalizeFirstChar(final String lowercaseWithSpaces) {
        return lowercaseWithSpaces.substring(0,1).toUpperCase() + lowercaseWithSpaces.substring(1).toLowerCase();
    }
}
