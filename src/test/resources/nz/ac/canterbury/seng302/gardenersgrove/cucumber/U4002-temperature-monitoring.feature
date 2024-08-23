Feature: As John Doe, I want to be able to tell the current and previous temperature of the garden.
  Background:
    Given I have a logged in user with a monitored garden

    Scenario: AC3 - I can see the average daily temp of the past 7 days
      Given I have a garden with a connected arduino
      And I am on the garden stats page
      When I choose to see a graph of the temperature in Degree Celsius over the last 7 days
      Then I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.

#    Scenario: AC4 - I can see the daily average over the past 30 days
#      Given I have a garden with a connected arduino
#      And I am on the garden stats page
#      When I choose to see a graph of the temperature in Degree Celsius over the last 30 days
#      Then I see a a display of the results for the average temperature for each day (average taken from each recorded 30 minute period).
#
#    Scenario: AC6 - The previously collected data appears on the graph
#      Given I have a garden with a disconnected arduino
#      When I view the garden stats page
#      Then all data that was previously collected still appears in the graph
