Feature: Login form with user attribute

  Scenario: user can log into account console
    Given keycloak is running with default setup
    When user goes to the account console page
    Then user should be not logged in
    When user clicks a sign in button
    Then login form with attribute input labeled as "Shoe size" should be shown
    When user log into account console with a valid credentials and user attribute equal "46"
    Then user should be logged into account console