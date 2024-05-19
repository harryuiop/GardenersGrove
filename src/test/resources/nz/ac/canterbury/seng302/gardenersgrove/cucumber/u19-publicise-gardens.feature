Feature: As Inaya, I want to be able to make my garden public so that others can see what I’m growing.

  Scenario: AC1 - A garden can be made public by checking a tick-box
    Given I am on the garden details page for a garden I own
    When I mark a checkbox labelled “Make my garden public
    Then my garden will be visible in search results

  Scenario: AC1 - A garden is not public if made public is not ticked
    Given I am on the garden details page for a garden I own
    When I do not mark a checkbox labelled Make my garden public
    Then my garden is not visible in search results

  Scenario: AC2 - I can add a description to my garden when I create it
    Given I am creating a new garden
    When I add an optional description of the garden
    And I submit the form
    And the description is valid
    Then the description is persisted

  Scenario: AC3 - I can edit and existing garden to add a description
    Given I am creating a new garden
    When I remove the description of the garden
    And I submit the form
    Then the description is deleted

  Scenario: AC4 - If the description is valid the description persists
    Given I am editing an existing garden
    When I add an optional description of the garden
    And I submit the form, and the description is valid
    Then the description is persisted

  Scenario: AC5 - When editing the garden and description is removed the description is removed
    Given I am editing an existing garden
    When I remove the description of the garden
    And I submit the form
    Then the description is deleted

  Scenario: AC6 - If a description is longer than 512 characters it is invalid
    Given I enter an invalid description (i.e. more than 512 characters long, or contains only special characters and numbers )
    When I submit the form
    Then an error message tells me that “Description must be "512" characters or less and contain some text”
    And the description is not persisted.

  Scenario: AC7 - If there is inappropriate language the description is invalid
    Given I enter a description that contains inappropriate words
    When I submit the form
    Then an error message tells me that “The description does not match the language standards of the app.”
    And the description is not persisted.

  Scenario: AC8 - I can see how many characters of the description that I have typed
    Given I enter some text into the description field
    Then I see an indication of the length of the input text such as “x/512” characters (where x is the current number of characters in the input)

  Scenario: AC9 - If the inappropriate langauge is not checked then the description persists but cannot be made public
    Given I enter a description
    When I submit the form
    Then I am informed my results was accepted but must be editited to be able to make public
    And the decription is persisted
    And I cannot make the garden public