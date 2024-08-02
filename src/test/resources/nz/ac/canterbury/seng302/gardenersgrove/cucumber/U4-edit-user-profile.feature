Feature: As Sarah, I want to edit my user profile so that I can keep my details accurate.

  Scenario: AC1 - User details are pre-populated on the edit profile page
    Given I have a user account that has logged in
    When I visit the edit profile page
    Then My details are populated into the fields except my password

  Scenario: AC2 - If I have no lastname, the "I have no surname" checkbox is checked on the edit profile page
    Given I have a user account that has logged in
    And My user account does not have a last name
    When I visit the edit profile page
    Then The "I have no surname" checkbox is marked as checked

  Scenario: AC3 - When I submit valid details, they are saved and I am taken to the profile page
    Given I have a user account that has logged in
    When I enter valid values for first name, last name, email, address and date of birth on the edit profile page
    And I click the submit button
    Then my new details are saved
    And I am taken to the profile page

  Scenario: AC4 - When I check the "I have no surname" checkbox and click submit, my last name is removed from my details
    Given I have a user account that has logged in
    And My user account has a saved last name
    When I check the "I have no surname" check box on the edit profile page
    And I click the submit button
    Then my old surname has been removed from my account

  Scenario: AC5 - When I submit an empty first name, I get an error message saying "First name cannot be empty"
    Given I have a user account that has logged in
    When I enter a blank value for first name on the edit profile page
    And I click the submit button
    Then an error message tells me "First name cannot be empty"

  Scenario: AC5 - When I submit an empty last name, I get an error message saying "Last name cannot be empty"
    Given I have a user account that has logged in
    When I enter a blank value for last name on the edit profile page
    And I click the submit button
    Then an error message tells me "Last name cannot be empty"

  Scenario Outline: AC5 - When I submit a first name with invalid characters, I get an error message explaining name validation rules
    Given I have a user account that has logged in
    When I enter the invalid <string> in the first name field on the edit profile page
    And I click the submit button
    Then I get an error message explaining the name validation rules
    Examples:
    | string  |
    | "sdf/]" |
    | "a"     |
    | "-sam"  |
    | "josh-" |

  Scenario Outline: AC5 - When I submit a last name with invalid characters, I get an error message explaining name validation rules
    Given I have a user account that has logged in
    When I enter the invalid <string> in the last name field on the edit profile page
    And I click the submit button
    Then I get an error message explaining the name validation rules
    Examples:
      | string  |
      | "sdf/]" |
      | "a"     |
      | "-sam"  |
      | "josh-" |

  Scenario: AC6 - When I submit a first name longer then 64 characters, I get an error message saying "First name must be 64 characters long or less"
    Given I have a user account that has logged in
    When I enter a name longer than 64 characters into the first name field
    And I click the submit button
    Then I get an error message saying "First name must be 64 characters long or less"

  Scenario: AC6 - When I submit a last name longer then 64 characters, I get an error message saying "Last name must be 64 characters long or less"
    Given I have a user account that has logged in
    When I enter a name longer than 64 characters into the last name field
    And I click the submit button
    Then I get an error message saying "Last name must be 64 characters long or less"

  Scenario: AC7 - When I submit an invalid email address, I get an error message saying "Email address must be in the form ‘jane@doe.nz’"
    Given I have a user account that has logged in
    When I enter a malformed <string> into the email field on the edit profile page
    And I click the submit button
    Then I get an error message saying "Email address must be in the form ‘jane@doe.nz’"
    Examples:
    | string      |
    | "asdoc"     |
    | "sam@gmail" |
    | "email.com" |

  Scenario: AC8 - When I submit an already used email address, I get an error message saying "This email address is already in use"
    Given I have a user account that has logged in
    When I enter an already used email address into the email field on the edit profile page
    And I click the submit button
    Then I get an error message saying "This email address is already in use"

  Scenario: AC10 - When I submit a date of birth younger then 13 years old, I get an error message saying "You must be 13 years or older to create an account"
    Given I have a user account that has logged in
    When I enter a date of birth younger then 13 years old in the date of birth field on the edit profile page
    And I click the submit button
    Then I get an error message saying "You must be 13 years or older to create an account"

  Scenario: AC11 - When I submit a date of birth older than 120 years old, I get an error message saying "The maximum age allowed is 120 years"
    Given I have a user account that has logged in
    When I enter a date of birth older than 120 years old in the date of birth field on the edit profile page
    And I click the submit button
    Then I get an error message saying "The maximum age allowed is 120 years"

  Scenario: AC12 - Clicking the cancel button discards any changes and takes me to the profile page
    Given I have a user account that has logged in
    When I visit the edit profile page
    And I enter any changes in the fields
    And I click the "Cancel" button
    Then The changes are not saved
    And I am taken to the profile page
