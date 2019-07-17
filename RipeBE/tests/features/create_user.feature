Feature: Handle creating, storing, and retrieving new user data
  Scenario: Create new user
    Given The user does not exist and some user information
    When I try to create a new user
    Then I should get a 200 response
      And the user should be in the mongoDB

  Scenario: Upload content to server
    Given The file exists along with content data
    When I try to upload content
    Then I should get a 200 response
      And the content data should be in the mongoDB