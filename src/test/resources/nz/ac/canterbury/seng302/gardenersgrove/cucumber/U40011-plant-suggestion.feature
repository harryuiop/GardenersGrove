Feature: As Sarah, I want to know plants that suit my garden so that I know the plants I get will have optimal growth.
  Scenario: AC2 - There is 14 days worth of arduino data so 3 plants are suggested based on that data
    Given there is 14 days worth of data from the Arduino,
    When the user clicks suggest plants on the view garden page,
    Then data is used to suggest 3 plants that would work well in the garden.