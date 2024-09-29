Feature: As Sarah, I want to guidance around what to do when my plants get too cold or hot so that I can keep my garden alive.
  Background:
    Given I have a logged in user with a monitored garden
    And I am on the garden stats page

  Scenario: AC1 - I see an ideal advice message
    When the temperature reading has stayed within some optimal temperature range for the past day
    Then I receive a message saying that the garden is currently at an ideal temperature

  Scenario: AC2 - I see an advice message for temperature below range
    When the temperature reading has gone below some minimum value in the last day
    Then I am shown a message informing me of symptoms to look for when plants get too cold

  Scenario: AC3 - I see an advice message for temperature above range
    When the temperature reading has gone above some maximum value in the last day
    Then I am shown a message informing me of symptoms to look for when plants get too hot

  Scenario: AC5 - I see an advice message for temperature below and above range
    When the temperature reading has gone both above the maximum value and below the minimum value in the last day
    Then I am shown a message informing me of the plant symptoms that occur during high temperature fluctuations
