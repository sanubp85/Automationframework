package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import pages.BasePage;
import utils.BrowserManager;
import utils.ConfigReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Hooks {
    private static BasePage basePage;
    
    @Before
    public void setUp() {
        addEnvironmentInfo();
        BrowserManager.initBrowser();
        basePage = new BasePage(BrowserManager.getPage());
        basePage.navigateTo(ConfigReader.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        String screenshotName = getScreenshotName(scenario);
        if (scenario.isFailed() && ConfigReader.isScreenshotOnFailure()) {
            byte[] screenshot = BrowserManager.getPage().screenshot();
            Allure.getLifecycle().addAttachment(screenshotName, "image/png", "png", screenshot);
        } else if (!scenario.isFailed() && ConfigReader.isScreenshotOnPass()) {
            byte[] screenshot = BrowserManager.getPage().screenshot();
            Allure.getLifecycle().addAttachment(screenshotName, "image/png", "png", screenshot);
        }
        BrowserManager.closeBrowser();
    }
    
    private String getScreenshotName(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.startsWith("@XrayID:")) {
                return tag.substring(1);
            }
        }
        return scenario.getName();
    }
    
    private void addEnvironmentInfo() {
        try {
            // Create allure-results directory if not exists
            java.nio.file.Path allureResultsPath = java.nio.file.Paths.get("target/allure-results");
            if (!java.nio.file.Files.exists(allureResultsPath)) {
                java.nio.file.Files.createDirectories(allureResultsPath);
            }
            
            // Copy executor.json to allure-results
            java.nio.file.Files.copy(
                getClass().getClassLoader().getResourceAsStream("executor.json"),
                java.nio.file.Paths.get("target/allure-results/executor.json"),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            
            // Add environment properties
            InputStream input = getClass().getClassLoader().getResourceAsStream("environment.properties");
            Properties prop = new Properties();
            prop.load(input);
            
            // Write environment.properties to allure-results
            java.io.FileWriter writer = new java.io.FileWriter("target/allure-results/environment.properties");
            prop.store(writer, "Environment Information");
            writer.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
