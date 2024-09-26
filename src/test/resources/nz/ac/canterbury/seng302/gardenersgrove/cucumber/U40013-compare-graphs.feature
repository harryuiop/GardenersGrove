Feature: As John, I want to compare my graphed data to other users so I can see how they vary.
  Background:
    Given I have a user that is logged in with a monitored garden
    And I have a garden with a connected Arduino

  Scenario: AC1 - From the monitor garden page of another user's garden, there is a dropdown to compare with any of my gardens.
    Given There is another user with a monitored garden and connected arduino
    When I navigate to another user's garden monitoring page for one of their gardens
    Then there is a dropdown containing the list of all my gardens to compare with the viewed garden

  Scenario: AC2 - From one of my garden's monitor garden page, there is a dropdown used to compare against any of my other gardens.
    Given I have a second garden with a connected Arduino
    When I navigate to the garden monitoring page for one of my gardens
    Then there is a dropdown containing all of my other gardens to compare with the viewed garden

  Scenario: AC3 - When I select a garden to compare my currently viewed garden with, then I am shown a page with the comparison of the two gardens.
    Given I have a second garden with a connected Arduino
    When I navigate to the garden monitoring page for one of my gardens
    And I select another of my gardens from the comparison dropdown
    Then I am shown a page with the comparison of both gardens

  Scenario: AC4 - When I am comparing two gardens, and I select 'No Comparison' from the comparison dropdown, I am taken back to my monitoring page for my garden.
    Given I have a second garden with a connected Arduino
    And I am on the comparison page between two of my gardens
    When I select the No Comparison option from the comparison dropdown
    Then I am taken back to the monitor garden page for my original garden
