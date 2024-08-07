Feature: As Kaia, I want to update the plants I have in my garden so that I can add more details about them as I know more about them.

  Background:
    Given I am logged in as "user1@gmail.com", "Password1!"
    And I have a garden
    And I am on the garden details page for a garden I own

    Scenario: AC1 - I have a list of all plants recorded with correct details
      Then there is a list of all plants I have recorded in the garden with their name, a default image, and count and description (if provided).

    Scenario: AC3 - Given I click the edit plant button next to any plant, then I see the edit plant form with the plant details prepopulated.
      When I click the edit plant button next to plant with id int 1
      Then I am taken to the edit plant page for plant with id int 1
      And the form values are prepopulated with the details of plant with id int 1



