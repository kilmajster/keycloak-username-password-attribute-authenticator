Feature: Login form with user attribute with environment variable based configuration

  Scenario: label is taken from environment variable
    Given keycloak is running with LOGIN_FORM_ATTRIBUTE_LABEL = "Custom label"
    When user goes to the account console page
    Then user should be not logged in
    When user clicks a sign in button
    Then login form with attribute input labeled as "Custom label" should be shown