package io.github.kilmajster.keycloak.utils;

public final class UserAttributeLabelGenerator {

    public static String generateLabel(final String attributeName) {
        final String lowercaseWithSpaces = attributeName
                .toLowerCase()
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ");

        return capitalizeFirstChar(lowercaseWithSpaces);
    }

    public static String generateErrorText(final String attributeName)  {
        return "Invalid " + generateLabel(attributeName).toLowerCase() + ".";
    }

    private static String capitalizeFirstChar(final String lowercaseWithSpaces) {
        return lowercaseWithSpaces.substring(0,1).toUpperCase() + lowercaseWithSpaces.substring(1).toLowerCase();
    }
}
