# Playwright Cucumber TestNG Automation Framework

A robust test automation framework built with Playwright, Cucumber, and TestNG for end-to-end testing with Allure reporting.

## 🚀 Features

- **Playwright** - Fast and reliable browser automation
- **Cucumber BDD** - Behavior-driven development with Gherkin syntax
- **TestNG** - Powerful test execution and management
- **Allure Reports** - Rich test reporting with screenshots
- **Multi-Browser Support** - Chrome, Edge, Firefox, and WebKit
- **Multi-Environment** - QA, UAT, Dev, and Prod configurations
- **Screenshot Management** - XrayID-based screenshot naming
- **Retry Mechanism** - Automatic retry for failed tests
- **Page Object Model** - Maintainable and scalable architecture

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Allure CLI (included in `.allure` folder)

## 🏗️ Project Structure

```
playwright-automation-framework/
├── src/
│   ├── main/java/
│   │   ├── pages/                  # Page Object classes
│   │   │   └── BasePage.java
│   │   └── utils/                  # All utility classes
│   │       ├── BrowserManager.java
│   │       ├── ConfigReader.java
│   │       ├── AllureRetryListener.java
│   │       ├── RetryAnalyzer.java
│   │       └── RetryListener.java
│   └── test/
│       ├── java/
│       │   ├── runners/            # TestNG runners
│       │   │   ├── TestRunner.java
│       │   │   └── FailedTestRunner.java
│       │   └── stepdefinitions/    # Cucumber step definitions
│       │       ├── Hooks.java
│       │       └── SampleSteps.java
│       └── resources/
│           ├── config/             # Environment configurations
│           │   ├── qa/
│           │   │   └── qa.properties
│           │   ├── dev/
│           │   │   └── dev.properties
│           │   ├── uat/
│           │   │   └── uat.properties
│           │   └── prod/
│           │       └── prod.properties
│           ├── features/           # Cucumber feature files
│           │   ├── sample.feature
│           │   └── ui/
│           │       └── ui.feature
│           ├── allure.properties
│           ├── categories.json
│           ├── environment.properties
│           └── executor.json
├── .allure/                        # Allure CLI
├── pom.xml
├── testng.xml
└── README.md
```

## ⚙️ Configuration

### Environment Properties (`src/test/resources/config/{env}/{env}.properties`)

Each environment only contains environment-specific settings:

**qa.properties**
```properties
base.url=https://example.com
headless=false
```

**prod.properties**
```properties
base.url=https://prod.example.com
headless=true
```

### Common Settings (`src/test/resources/environment.properties`)

Browser and other common settings are maintained here and apply to all environments:

```properties
Project=Automation Project
Environment=QA
Browser=Chrome
Test.Framework=Cucumber+TestNG
Language=Java
Retry.Enabled=false
Retry.Count=2
Screenshot.OnFailure=true
Screenshot.OnPass=true
```

## 🎯 Key Features

### 1. XrayID-Based Screenshot Naming

Screenshots are automatically named based on the `@XrayID` tag in feature files:

```gherkin
@XrayID:1234
Scenario: Navigate to a website
  Then I should see URL contains "example.com"
```

- **With XrayID**: Screenshot named as `XrayID:1234`
- **Without XrayID**: Screenshot named as scenario name

### 2. Multi-Browser Support

Supported browsers:
- Chrome (default)
- Edge
- Firefox
- WebKit

Configure browser in `environment.properties` — applies to all environments:
```properties
Browser=Chrome
```

Supported values: `chrome`, `chromium`, `edge`, `firefox`, `webkit`

### 3. Multi-Environment Support

Switch environments using Maven:
```bash
mvn test -Denv=qa
mvn test -Denv=uat
mvn test -Denv=dev
mvn test -Denv=prod
```

### 4. Retry Mechanism

Enable retry for failed tests in `environment.properties`:
```properties
Retry.Enabled=true
Retry.Count=2
```

## 🚦 Running Tests

### Run all tests
```bash
mvn clean test
```

### Run with specific environment
```bash
mvn clean test -Denv=qa
```

### Run in headless mode
Update in env properties file:
```properties
headless=true
```

### Run specific feature
```bash
mvn test -Dcucumber.features="src/test/resources/features/sample.feature"
```

### Run with tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run with full cmd
```bash
mvn clean test "-Denv=qa" "-DRetry.Enabled=true" "-DRetry.Count=1" "-Dcucumber.filter.tags=@p1"
```

## 📊 Allure Reports

### Generate and open report
```bash
mvn allure:report
mvn allure:serve
```

### Using batch file
```bash
open-allure-report.bat
```

### Report Features
- Test execution summary
- Test case details with steps
- Screenshots on failure/pass
- Environment information
- Execution timeline
- Retry history
- Categories and trends

## 📝 Writing Tests

### Feature File Example
```gherkin
@regression
Feature: Sample Test

  @smoke @p1 @XrayID:1234
  Scenario: Navigate to a website
    Then I should see URL contains "example.com"
```

### Step Definition Example
```java
@Then("I should see URL contains {string}")
public void verifyUrlContains(String expectedUrl) {
    String actualUrl = BrowserManager.getPage().url();
    assert actualUrl.contains(expectedUrl);
}
```

### Page Object Example
```java
public class LoginPage extends BasePage {
    public LoginPage(Page page) {
        super(page);
    }
    
    public void login(String username, String password) {
        fill("#username", username);
        fill("#password", password);
        click("#loginBtn");
    }
}
```

## 🛠️ Utilities

### BrowserManager
- `initBrowser()` - Initialize browser
- `getPage()` - Get current page instance
- `closeBrowser()` - Close browser and cleanup

### ConfigReader
- `getBaseUrl()` - Get base URL from env properties
- `getBrowser()` - Get browser name from environment.properties
- `isHeadless()` - Check headless mode from env properties
- `isRetryEnabled()` - Check retry enabled from environment.properties
- `isScreenshotOnFailure()` - Check screenshot on failure
- `isScreenshotOnPass()` - Check screenshot on pass

### BasePage Methods
- `navigateTo(url)` - Navigate to URL
- `click(selector)` - Click element
- `fill(selector, text)` - Fill text
- `getText(selector)` - Get text
- `isVisible(selector)` - Check visibility
- `waitForSelector(selector)` - Wait for element
- `assertVisible(selector)` - Assert visibility
- `assertTextEquals(selector, text)` - Assert text

## 📦 Dependencies

- Playwright 1.40.0
- Cucumber 7.14.0
- TestNG 7.8.0
- Allure 2.24.0

## 🔧 Troubleshooting

### Browser not launching
- Ensure Playwright browsers are installed
- Check `Browser` value in `environment.properties` (supported: `chrome`, `edge`, `firefox`, `webkit`)

### Tests not running
- Verify TestNG XML configuration
- Check feature file paths in runner

### Allure report not generating
- Ensure Allure CLI is in `.allure` folder
- Check `allure-results` directory exists

### mvn clean fails
- A previous run may still be holding a file lock
- Close any open browser windows or reports and retry

## 👤 Author

**Prasanna Kumar**

## 📄 License

This project is licensed under the MIT License.
