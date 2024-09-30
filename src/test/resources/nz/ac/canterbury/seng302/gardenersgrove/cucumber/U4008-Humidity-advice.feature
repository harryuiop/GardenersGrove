Feature: As Sarah, I want to know the ideal humidity for my garden so that I know whether to water my plants or not.
  Background:
    Given I have a logged in user with a monitored garden
    And I am on the garden stats page

  Scenario: AC1 - I see an ideal advice message
    When the humidity reading has stayed within some optimal temperature range for the past day
    Then I am shown a message saying that the garden is currently at an ideal humidity.

  Scenario: AC2 - I see an advice message for humidity below range
    When the humidity reading has gone below some minimum value in the last day
    Then I am shown a message informing me of symptoms to look for when the humidity is too low.

  Scenario: AC3 - I see an advice message for humidity above range
    When the humidity reading has gone above some maximum value in the last day
    Then I am shown a message informing me of symptoms to look for when the humidity is too high.

  Scenario: I see an advice message for humidity above and below range
    When the humidity reading has gone above some maximum and below some minimum value in the last day
    Then I am shown a message informing me of symptoms to look for when the humidity is too high and too low.