@regression
Feature: Sample Test

  @smoke @p1 @XrayID:1234
  Scenario: Navigate to a website
    Then I should see URL contains "example.com"
@p4
  Scenario: Navigate to a website1
    Then I should see URL contains "e.ceeem"
@p1
  Scenario: Navigate to a website2
    Then I should see URL contains "example.com"
