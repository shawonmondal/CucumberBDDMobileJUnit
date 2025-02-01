#@Test
Feature: Login Scenarios

  @Test
  Scenario Outline: Login with invalid username
#    Given I am on the login page
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I click on the login button
    Then I should see the error message "<ErrorMessage>"

    Examples:
      | username        | password     | ErrorMessage                                                 |
      | invalidUsername | secret_sauce | Username and password do not match any user in this service. |


  Scenario Outline: Login with invalid password
#    Given I am on the login page
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I click on the login button
    Then I should see the error message "<ErrorMessage>"

    Examples:
      | username      | password       | ErrorMessage                                                 |
      | standard_user | wrong_Password | Username and password do not match any user in this service. |


  Scenario Outline: Login with invalid username and password
#    Given I am on the login page
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I click on the login button
    Then I should see the error message "<ErrorMessage>"

    Examples:
      | username        | password       | ErrorMessage                                                 |
      | invalidUsername | wrong_Password | Username and password do not match any user in this service. |

  @Test
  Scenario Outline: Login with valid username and password
#    Given I am on the login page
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I click on the login button
    Then I should see the products page title "<title>"

    Examples:
      | username      | password     | title    |
      | standard_user | secret_sauce | PRODUCTS |