package stepdefinitions;

import io.cucumber.java.en.*;
import pages.BasePage;
import utils.BrowserManager;
import utils.ConfigReader;

public class SampleSteps {
    private BasePage basePage;

    @Given("I navigate to base url")
    public void navigateToBaseUrl() {
        basePage = new BasePage(BrowserManager.getPage());
        basePage.navigateTo(ConfigReader.getBaseUrl());
    }

    @Given("I navigate to {string}")
    public void navigateToUrl(String url) {
        basePage = new BasePage(BrowserManager.getPage());
        basePage.navigateTo(url);
    }

    @When("I click on {string}")
    public void clickElement(String selector) {
        basePage.click(selector);
    }

    @Then("I should see {string}")
    public void verifyElementVisible(String selector) {
        assert basePage.isVisible(selector);
    }

    @Then("I should see URL contains {string}")
    public void verifyUrlContains(String expectedUrl) {
        String actualUrl = BrowserManager.getPage().url();
        assert actualUrl.contains(expectedUrl) : "Expected URL to contain: " + expectedUrl + ", but was: " + actualUrl;
    }
}
