Feature: As Sarah, I want to know how moist my soil is so I know when to water it.
  Background:
    Given I have a logged in user with a monitored garden
    And I am on the garden stats page

  Scenario: AC1 - I see an ideal advice message
    When the soil moisture reading has stayed within some optimal temperature range for the past day
    Then I am shown a message saying that the garden is at an ideal moisture.

  Scenario: AC2 - I see an advice message for soil moisture below range
    When the soil moisture reading has gone below some minimum value in the last day
    Then I am shown a message that tells me to water my garden

  Scenario: AC3 - I see an advice message for soil moisture above range
    When the soil moisture reading has gone above some maximum value in the last day
    Then I am shown a message providing tips for very moist soil.
