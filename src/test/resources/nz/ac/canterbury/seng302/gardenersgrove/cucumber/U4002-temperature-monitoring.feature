Feature: As John Doe, I want to be able to tell the current and previous temperature of the garden.
  Background:
    Given I have a logged in user with a monitored garden
    Given I have a garden with a connected arduino
    And I am on the garden stats page


  Scenario: AC1 - Given I have a garden with a connected Arduino when I go to the garden stats page then I can see the current temperature (within the previous 5 minutes) in either degrees Celsius or Fahrenheit
    Then I see the current temperature

  Scenario: AC2 - I can see the average daily temp of the past day
    When I choose to see a graph of the temperature in Degree Celsius over the last day
    Then I see a display of results for the average temperature for each half hour of that day.

    Scenario: AC3 - I can see the average daily temp of the past 7 days
      When I choose to see a graph of the temperature in Degree Celsius over the last seven days
      Then I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.

    Scenario: AC4 - I can see the daily average over the past 30 days
      When I choose to see a graph of the temperature in Degree Celsius over the last thirty days
      Then I see a a display of the results for the average temperature for each day.
