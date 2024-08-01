Feature: As Inaya, I want to be able to make my garden public so that others can see what I’m growing.
  Background:
    Given I have a user account that has logged in
    And I have a garden

  Scenario: AC1 - A garden can be made public by checking a tick-box
    And I am on the garden details page for a garden I own
    When I mark a checkbox labelled “Make my garden public“
    Then my garden will be visible in search results

  Scenario: AC1 - A garden is not public if made public is not ticked
    Then my garden is not visible in search results

  Scenario: AC2 - I can add a description to my garden when I create it
    When I add an optional description of the garden
    And I submit the form
    And the description is valid
    Then the description is persisted

  Scenario: AC3 - I can edit an existing garden to add a description
    And I am creating a new garden
    When I remove the description of the garden
    And I submit the form
    Then the description is deleted

  Scenario: AC4 - If the description is valid the description persists
    And I am editing an existing garden
    When I add an optional description of the garden
    And I submit the form
    And the description is valid
    Then the description is persisted

  Scenario: AC5 - When editing the garden and description is removed the description is removed
    And I am editing an existing garden
    When I remove the description of the garden
    And I submit the form
    Then the description is deleted

  Scenario: AC6 - If a description is longer than 512 characters it is invalid
    And I enter a description longer than 512 characters
    When I submit the form
    Then an error message tells me that “Description must be "512" characters or less and contain some text”
    And the description is not persisted.

  Scenario Outline: AC6 - If a description is contains no valid characters it is invalid
    And I enter a description <string>
    When I submit the form
    Then an error message tells me that “Description must be "512" characters or less and contain some text”
    And the description is not persisted.
    Examples:
    | string |
    |  " "   |
    | "$%^&" |
    | "1234" |


  Scenario Outline: AC7 - If there is inappropriate language the description is invalid
    And I enter a description that contains inappropriate words <string>
    When I submit the form
    Then an error message tells me that “The description does not match the language standards of the app.”
    And the description is not persisted.
    Examples:
    | string                |
    | "Fuck"                |
    | "Shit"                |
    | "Supcalifragalisdick" |
    | "@sshole"             |

  Scenario: AC9 - If the inappropriate language is not checked then the description persists but cannot be made public
    And I enter some text into the description field
    When I submit the form
    Then I am informed my results was accepted but must be edited to be able to make public
    And the description is persisted
    And I cannot make the garden public