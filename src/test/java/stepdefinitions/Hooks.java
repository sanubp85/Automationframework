package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import pages.BasePage;
import utils.BrowserManager;
import utils.ConfigReader;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class Hooks {
    private static BasePage basePage;
    private static final Map<String, Integer> retryCountMap = new HashMap<>();
    private static final Map<String, String> historyIdMap = new HashMap<>();

    @Before
    public void setUp(Scenario scenario) {
        addEnvironmentInfo();
        String scenarioId = scenario.getId();
        int retryCount = retryCountMap.getOrDefault(scenarioId, 0);
        if (retryCount > 0) {
            int maxRetry = Integer.parseInt(System.getProperty("Retry.Count", "2"));
            Allure.step("Retry attempt #" + retryCount + " of " + maxRetry);
        }
        BrowserManager.initBrowser();
        basePage = new BasePage(BrowserManager.getPage());
        basePage.navigateTo(ConfigReader.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        String screenshotName = getScreenshotName(scenario);
        String scenarioId = scenario.getId();

        if (scenario.isFailed()) {
            int currentRetry = retryCountMap.getOrDefault(scenarioId, 0);
            retryCountMap.put(scenarioId, currentRetry + 1);

            if (ConfigReader.isScreenshotOnFailure()) {
                byte[] screenshot = BrowserManager.getPage().screenshot();
                Allure.getLifecycle().addAttachment(screenshotName, "image/png", "png", screenshot);
            }

            // Capture historyId from first run
            if (!historyIdMap.containsKey(scenarioId)) {
                Allure.getLifecycle().getCurrentTestCase().ifPresent(uuid ->
                    Allure.getLifecycle().updateTestCase(uuid, tc ->
                        historyIdMap.put(scenarioId, tc.getHistoryId())
                    )
                );
            }

            // Write a separate result JSON for each retry so Allure Retries tab shows all attempts
            if (currentRetry > 0) {
                writeRetryResultJson(scenario, scenarioId, currentRetry);
            }
        } else {
            retryCountMap.remove(scenarioId);
            historyIdMap.remove(scenarioId);
            if (ConfigReader.isScreenshotOnPass()) {
                byte[] screenshot = BrowserManager.getPage().screenshot();
                Allure.getLifecycle().addAttachment(screenshotName, "image/png", "png", screenshot);
            }
        }
        BrowserManager.closeBrowser();
    }

    private void writeRetryResultJson(Scenario scenario, String scenarioId, int retryCount) {
        try {
            int maxRetry = Integer.parseInt(System.getProperty("Retry.Count", "2"));
            String historyId = historyIdMap.getOrDefault(scenarioId, scenarioId);
            String uuid = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            String json = "{" +
                "\"uuid\":\"" + uuid + "\"," +
                "\"historyId\":\"" + historyId + "\"," +
                "\"name\":\"" + scenario.getName() + "\"," +
                "\"fullName\":\"" + scenarioId + "\"," +
                "\"status\":\"failed\"," +
                "\"statusDetails\":{\"flaky\":true,\"message\":\"Retry attempt " + retryCount + " of " + maxRetry + "\"}," +
                "\"labels\":[" +
                    "{\"name\":\"feature\",\"value\":\"" + scenario.getId().split(";")[0] + "\"}," +
                    "{\"name\":\"story\",\"value\":\"" + scenario.getName() + "\"}," +
                    "{\"name\":\"Retry\",\"value\":\"Attempt " + retryCount + " of " + maxRetry + "\"}" +
                "]," +
                "\"start\":" + (now - 1000) + "," +
                "\"stop\":" + now +
            "}";

            Files.write(Paths.get("target/allure-results/" + uuid + "-result.json"), json.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
