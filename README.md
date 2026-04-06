# FSGTools Automation Framework

A robust end-to-end test automation framework for UI and API testing, built on **Playwright**, **Cucumber BDD**, and **TestNG** with integrated **Allure** reporting.

---

## Technology Stack

| Component         | Technology              | Version   |
|-------------------|-------------------------|-----------|
| Language          | Java                    | 11+       |
| Build Tool        | Maven                   | 3.6+      |
| Browser Automation| Microsoft Playwright    | 1.40.0    |
| BDD Framework     | Cucumber                | 7.14.0    |
| Test Runner       | TestNG                  | 7.8.0     |
| API Testing       | REST Assured            | 5.3.2     |
| Reporting         | Allure                  | 2.24.0    |
| Allure CLI        | Allure Commandline      | 2.20.1    |

---

## Project Structure

```
FSGTool-automation/
├── src/
│   ├── main/java/
│   │   ├── api/
│   │   │   └── ApiClient.java          # REST Assured HTTP client (GET/POST with auth)
│   │   ├── pages/
│   │   │   └── BasePage.java           # Base page with Playwright actions & assertions
│   │   └── utils/
│   │       ├── AllureRetryListener.java # TestNG listener for Allure retry tracking
│   │       ├── BrowserManager.java     # Thread-safe browser lifecycle management
│   │       └── ConfigReader.java       # Environment-aware properties loader
│   └── test/
│       ├── java/
│       │   ├── runners/
│       │   │   └── TestRunner.java     # Cucumber-TestNG runner with parallel support
│       │   └── stepdefinitions/
│       │       ├── Hooks.java          # Before/After hooks, screenshots, retry logic
│       │       └── SampleSteps.java    # Sample step definitions
│       └── resources/
│           ├── config/
│           │   ├── dev/dev.properties
│           │   ├── qa/qa.properties
│           │   ├── uat/uat.properties
│           │   └── prod/prod.properties
│           ├── features/
│           │   ├── ui/                 # UI feature files
│           │   ├── api/                # API feature files
│           │   └── sample.feature
│           ├── allure.properties
│           ├── categories.json
│           ├── environment.properties  # Global framework settings
│           └── executor.json
├── auth/
│   └── storageState.json               # Playwright session/auth state
├── testng.xml                          # TestNG suite definition
├── testng-no-retry.xml                 # Suite without retry listener
├── open-allure-report.bat              # Script to open Allure report
└── pom.xml
```

---

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Browsers: Chromium, Firefox, or WebKit (auto-managed by Playwright)

---

## How to Run

**Run the full test suite (default: QA environment, Chrome):**
```bash
mvn clean test
```

**Run with a specific environment:**
```bash
mvn clean test -Denv=qa
mvn clean test -Denv=uat
mvn clean test -Denv=prod
```

**Run with a specific browser:**
```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
mvn clean test -Dbrowser=webkit
```

**Run with parallel threads:**
```bash
mvn clean test -DthreadCount=3
```

**Run with retry on failure:**
```bash
mvn clean test -DRetry.Enabled=true -DRetry.Count=2
```

**Open Allure report after test run:**
```bash
open-allure-report.bat
```
Or via Maven:
```bash
mvn allure:report
```

---

## Configuration

### Environment Properties
Each environment has its own config file at `src/test/resources/config/{env}/{env}.properties`:

```properties
base.url=https://example.com
headless=false
```

### Global Framework Settings (`environment.properties`)

```properties
Project=FSGTools
Environment=QA
Browser=chrome
Test.Framework=Cucumber+TestNG
Language=Java
Retry.Enabled=false
Retry.Count=2
Screenshot.OnFailure=true
Screenshot.OnPass=true
```

---

## Key Features

- **Multi-browser support** — Chromium, Firefox, Edge, and WebKit via Playwright
- **BDD with Cucumber** — Feature files in Gherkin syntax under `features/ui/` and `features/api/`
- **Parallel execution** — Scenarios run in parallel via TestNG `DataProvider`
- **Thread-safe browser management** — `BrowserManager` uses `ThreadLocal` for isolated browser instances per thread
- **API testing** — `ApiClient` wraps REST Assured for GET/POST with Bearer token support
- **Auto retry** — Failed scenarios are retried based on `Retry.Count`; retry attempts are tracked in Allure
- **Allure reporting** — Rich HTML reports with history trends, categories, screenshots, and environment info
- **Screenshot capture** — Configurable on failure and/or on pass, attached directly to Allure report
- **Session state persistence** — Playwright `storageState.json` for reusing authenticated sessions
- **Rerun support** — Failed scenario URIs written to `target/rerun.txt` for targeted reruns

---

## Writing Tests

### 1. Create a Feature File
```gherkin
# src/test/resources/features/ui/login.feature
Feature: Login

  @ui
  Scenario: Successful login
    Given I navigate to the login page
    When I enter valid credentials
    Then I should see the dashboard
```

### 2. Create a Page Object
```java
public class LoginPage extends BasePage {
    public LoginPage(Page page) { super(page); }

    public void login(String username, String password) {
        fill("#username", username);
        fill("#password", password);
        click("#loginBtn");
    }
}
```

### 3. Implement Step Definitions
```java
@Given("I navigate to the login page")
public void navigateToLogin() {
    basePage.navigateTo(ConfigReader.getBaseUrl());
}
```

### 4. Tag API scenarios with `@api`
Scenarios tagged `@api` skip browser initialization and use `ApiClient` directly.

---

## Reporting

Allure report is generated at `target/allure-report/index.html` after each run.

Report includes:
- Test execution summary and pass/fail trends
- Per-scenario timeline and duration
- Failure screenshots attached inline
- Retry attempt history
- Environment and executor metadata

---

## Authors

- Sanu B P
