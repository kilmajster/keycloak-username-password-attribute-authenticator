@ignore
Feature: Login form with user attribute and environment variable based configuration

  Scenario: label and error message are generated from attribute name when environment variable configuration is empty
    Given keycloak is running with default setup
    When user navigates to login page
    Then login form with attribute input labeled as "Foot size" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid foot size." is present
    And attempted username is cleared

  Scenario: attempted username is not cleared when clearing is disabled via environment variable
    Given keycloak is running with CLEAR_USER_ON_ATTRIBUTE_VALIDATION_FAIL = false
    When user navigates to login page
    Then login form with attribute input labeled as "Foot size" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid foot size." is present
    And attempted username is set to "test"
    And restart login link is visible

  Scenario: label and error message are generated from attribute name when attribute is taken from environment variable
    Given keycloak is running with LOGIN_FORM_USER_ATTRIBUTE = VERY_custom-uSeR.ATTRIBUTE
    When user navigates to login page
    Then login form with attribute input labeled as "Very custom user attribute" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid very custom user attribute." is present
    And attempted username is cleared

  Scenario: label and error message are taken from attribute name without prettify
    Given keycloak is running with LOGIN_FORM_GENERATE_LABEL = false
    When user navigates to login page
    Then login form with attribute input labeled as "foot_size" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid foot_size" is present
    And attempted username is cleared

  Scenario: label and error message are taken from environment variable
    Given keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = Custom user attribute
    When user navigates to login page
    Then login form with attribute input labeled as "Custom user attribute" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid custom user attribute." is present
    And attempted username is cleared

  Scenario: label and error message are taken from message resolved from environment variable
    Given keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = login_form_attribute_label_default
    When user navigates to login page
    Then login form with attribute input labeled as "User attribute" should be shown
    When user log into account console with a valid credentials and user attribute equal "invalid-user-attribute"
    Then form error with message "Invalid user attribute." is present
    And attempted username is cleared