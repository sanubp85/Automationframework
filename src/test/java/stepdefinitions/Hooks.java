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
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class Hooks {
    private static BasePage basePage;
    private static final Map<String, Integer> retryCountMap = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<String, String> historyIdMap = new java.util.concurrent.ConcurrentHashMap<>();
    public static final java.util.concurrent.ConcurrentLinkedQueue<String> failedScenarioUris = new java.util.concurrent.ConcurrentLinkedQueue<>();

    @Before
    public void setUp(Scenario scenario) {
        addEnvironmentInfo();
        if (scenario.getSourceTagNames().contains("@api")) return;
        String scenarioId = scenario.getId();
        int retryCount = retryCountMap.getOrDefault(scenarioId, 0);
        if (retryCount > 0) {
            int maxRetry = ConfigReader.getRetryCount();
            Allure.step("Retry attempt #" + retryCount + " of " + maxRetry);
        }
        BrowserManager.initBrowser();
        basePage = new BasePage(BrowserManager.getPage());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@api")) return;
        String screenshotName = getScreenshotName(scenario);
        String scenarioId = scenario.getId();

        if (scenario.isFailed()) {
            int currentRetry = retryCountMap.getOrDefault(scenarioId, 0);
            retryCountMap.put(scenarioId, currentRetry + 1);

            // Collect failed URI for rerun.txt (flushed by TestRunner.@AfterClass)
            try {
                String uri = scenario.getUri().toString();
                if (uri.startsWith("file:")) {
                    uri = new java.io.File(new java.net.URI(uri)).getAbsolutePath();
                } else if (uri.startsWith("classpath:")) {
                    uri = "src/test/resources/" + uri.substring("classpath:".length());
                }
                String entry = uri + ":" + scenario.getLine();
                failedScenarioUris.add(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ConfigReader.isScreenshotOnFailure()) {
                byte[] screenshot = BrowserManager.getPage().screenshot();
                Allure.getLifecycle().addAttachment(screenshotName, "image/png", "png", screenshot);
            }

            if (!historyIdMap.containsKey(scenarioId)) {
                Allure.getLifecycle().getCurrentTestCase().ifPresent(uuid ->
                    Allure.getLifecycle().updateTestCase(uuid, tc ->
                        historyIdMap.put(scenarioId, tc.getHistoryId())
                    )
                );
            }

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
            int maxRetry = ConfigReader.getRetryCount();
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

    private static volatile boolean envInfoWritten = false;

    private void addEnvironmentInfo() {
        if (envInfoWritten) return;
        synchronized (Hooks.class) {
            if (envInfoWritten) return;
            try {
                java.nio.file.Path allureResultsPath = java.nio.file.Paths.get("target/allure-results");
                if (!java.nio.file.Files.exists(allureResultsPath)) {
                    java.nio.file.Files.createDirectories(allureResultsPath);
                }
                java.nio.file.Files.copy(
                    getClass().getClassLoader().getResourceAsStream("executor.json"),
                    java.nio.file.Paths.get("target/allure-results/executor.json"),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                InputStream input = getClass().getClassLoader().getResourceAsStream("environment.properties");
                Properties prop = new Properties();
                prop.load(input);
                input.close();

                // Use ConfigReader as single source of truth — run command values take priority, fallback to environment.properties
                prop.setProperty("Environment", System.getProperty("env", prop.getProperty("Environment", "qa")).toUpperCase());
                prop.setProperty("Browser", ConfigReader.getBrowser());
                prop.setProperty("Retry.Enabled", String.valueOf(ConfigReader.isRetryEnabled()));
                prop.setProperty("Retry.Count", String.valueOf(ConfigReader.getRetryCount()));

                java.io.FileWriter writer = new java.io.FileWriter("target/allure-results/environment.properties");
                prop.store(writer, "Environment Information");
                writer.close();
                envInfoWritten = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
