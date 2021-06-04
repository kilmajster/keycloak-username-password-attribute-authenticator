Feature: Login form with user attribute and environment variable based configuration

  Scenario: label is generated from attribute name when environment variable configuration is empty
    Given keycloak is running with default setup
    When user navigates to login page
    Then login form with attribute input labeled as "Test attr" should be shown

  Scenario: label is generated from attribute name when attribute is taken from environment variable configuration
    Given keycloak is running with LOGIN_FORM_USER_ATTRIBUTE = "CUSTOM-user_AtTribUTte"
    When user navigates to login page
    Then login form with attribute input labeled as "Custom user attribute" should be shown

  Scenario: label is taken from attribute name without prettify
    Given keycloak is running with LOGIN_FORM_GENERATE_LABEL = false
    When user navigates to login page
    Then login form with attribute input labeled as "test_attr" should be shown

  Scenario: label is taken from environment variable
    Given keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = "Custom label"
    When user navigates to login page
    Then login form with attribute input labeled as "Custom label" should be shown

  Scenario: label is taken from message resolved from environment variable key
    Given keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = "login_form_attribute_label_default"
    When user navigates to login page
    Then login form with attribute input labeled as "User attribute" should be shown