@ui @regression
Feature: UI Test Scenarios

  @smoke @p1 @XrayID:1234
  Scenario: Navigate to a website
    Given I navigate to base url
    Then I should see URL contains "example.com"

  @p1 @XrayID:1235
  Scenario: Verify page title
    Given I navigate to base url
    Then I should see URL contains "example.com"
