@api @regression
Feature: API Test Scenarios

  @smoke @p1 @XrayID:2001
  Scenario: Verify GET API response
    Given I send GET request to "/api/users"
    Then the response status code should be 200
    And the response should contain "data"

  @p2 @XrayID:2002
  Scenario: Verify POST API request
    Given I send POST request to "/api/users" with body
      """
      {
        "name": "Test User",
        "email": "test@example.com"
      }
      """
    Then the response status code should be 201
    And the response should contain "id"
