Feature: As Kaia, I want to update the plants I have in my garden so that I can add more details about them as I know more about them.

  Background:
    Given I have a user account that has logged in as "user1@gmail.com", "Password1!"
    And I am on the garden details page for a garden I own

    Scenario: I have a list of all plants recorded with correct details
      Then there is a list of all plants I have recorded in the garden with their name, a default image, and count and description (if provided).