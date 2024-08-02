Feature: As Liam, I want to cancel friends on Gardener’s Grove so that we can manage my friends list with people I trust.

  Background:
    Given I have two users
    And I have a user account that has logged in as "user1@mail.com", "Password1!"


  Scenario: AC1 - Given I am on my “manage friends” page, when I have pending request that I have sent, then I
  can cancel my friend request, and the other user cannot see the friend request, and the other user
  cannot accept the request anymore.
    Given I am on the manage friends page
    And I have have sent a friend request
    When I cancel my friend request
    Then The other user cannot accept the request anymore

