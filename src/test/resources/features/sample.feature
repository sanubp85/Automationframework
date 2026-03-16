@regression
Feature: Sample Test

  @smoke @p1 @XrayID:123456
  Scenario: Navigate to a website
    Then I should see URL contains "google.com"
@p4 @retryfail
  Scenario: Navigate to a website1
    Then I should see URL contains "google.co.in"
@p1
  Scenario: Navigate to a website2
    Then I should see URL contains "google.com"
