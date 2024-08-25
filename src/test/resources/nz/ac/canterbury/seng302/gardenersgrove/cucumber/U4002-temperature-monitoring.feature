Feature: As John Doe, I want to be able to tell the current and previous temperature of the garden.
  Background:
    Given I have a logged in user with a monitored garden
    Given I have a garden with a connected arduino
    And I am on the garden stats page


  Scenario: AC2 - I can see the average daily temp of the past 7 days
    When I choose to see a graph of the temperature in Degree Celsius over the last day
    Then I see a a display of results for the average temperature for each half hour of that day.

    Scenario: AC3 - I can see the average daily temp of the past 7 days
      When I choose to see a graph of the temperature in Degree Celsius over the last seven days
      Then I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.

    Scenario: AC4 - I can see the daily average over the past 30 days
      When I choose to see a graph of the temperature in Degree Celsius over the last thirty days
      Then I see a a display of the results for the average temperature for each day.
