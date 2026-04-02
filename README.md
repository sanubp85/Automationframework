# Java Automation Framework Skeleton

A clean automation framework skeleton built with Playwright, Cucumber, and TestNG.

This repository is now ready for a fresh project upload. It contains the minimal automation framework structure and sample configuration needed to start new UI or API tests.

## ? Included structure

- `src/main/java/pages` - base page class
- `src/main/java/utils` - browser, config, and reporting helpers
- `src/test/java/runners` - Cucumber TestNG runner
- `src/test/java/stepdefinitions` - sample step definitions and hooks
- `src/test/resources/config` - environment-specific properties
- `src/test/resources/features` - starter sample feature
- `testng.xml` - test suite definition
- `pom.xml` - Maven build and dependencies

## ?? Prerequisites

- Java 11 or higher
- Maven 3.6+

## ?? How to run

Run the sample suite:
```bash
mvn clean test
```

Run with a specific environment:
```bash
mvn clean test -Denv=qa
```

## ?? Configuration

Environment files are stored in `src/test/resources/config/{env}/{env}.properties`.

Example:
```properties
base.url=https://example.com
headless=false
```

Common framework settings are in `src/test/resources/environment.properties`.

## ?? Sample feature

The starter feature is located at `src/test/resources/features/sample.feature`.

## ?? Notes

- Old project-specific test cases and page objects were removed.
- The framework is ready for new project-specific pages, step definitions, and feature files.
- The existing utilities can be extended for browser setup, configuration, and reporting.
