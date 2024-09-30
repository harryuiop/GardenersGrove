Feature: As Sarah, I want to know plants that suit my garden so that I know the plants I get will have optimal growth.
  Scenario: AC2 - There is 14 days worth of arduino data so 3 plants are suggested based on that data
    Given there is 14 days worth of data from the Arduino,
    When the user clicks 'Plant Suggestions' on the view garden page,
    Then data is used to suggest 3 plants that would work well in the garden.

  Scenario: AC3 - As Sarah, I want to be able to get plant suggestions for my garden based on the location if
    I don't have an Arduino connected to my garden or the data from my garden with a connected Arduino is from more
    than 14 days ago
    Given there is no Arduino connected (or less than 14 days of data) but there is a valid location for the garden,
    When the user clicks 'Plant Suggestions' on the view garden page,
    Then data is used to suggest 3 plants that would work well in the garden.

