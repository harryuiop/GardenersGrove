Feature: As Sarah, I want to know if I need to adjust the plant location so that they get the right amount of sunlight to grow.
  Background:
    Given I have a logged in user with a monitored garden
    And I am on the garden stats page

  Scenario: AC1 - I receive a full sun advice message that describes the situation
    When I am received a light reading that is above and equal to 50 percent for more than 6 hours
    Then I receive a message saying that the garden is currently receiving full sun light

  Scenario: AC2 - I receive a partial sun advice message that describes the situation
    When I am received a light reading that is above and equal to 50 percent for more than 2 hours and less than 6 hours
    Then I receive a message saying that the garden is currently receiving partial sun light

  Scenario: AC3 - I receive a partial shade advice message that describes the situation
    When I am received a light reading that is above and equal to 25 percent for more than 2 hours and less than 6 hours
    Then I receive a message saying that the garden is currently receiving partial shade light

  Scenario: AC4 - I receive a full shade advice message that describes the situation
    When I am received a light reading that is above and equal to 25 percent for more than 6 hours
    Then I receive a message saying that the garden is currently receiving full shade light