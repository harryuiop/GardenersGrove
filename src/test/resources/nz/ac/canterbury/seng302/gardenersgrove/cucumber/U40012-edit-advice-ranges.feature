Feature: As Inaya, I would like to be able to change to data ranges that affect a garden's advice so that it so that it better reflects my garden.

  Background:
    Given I am logged in to gardeners grove
    And I am on the garden monitoring page for my own garden

  Scenario:
    When I open the settings modal
    Then I can see the advice ranges are pre-populated with the current ranges
