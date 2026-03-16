package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "@target/rerun.txt",
        glue = "stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-reports.html", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "rerun:target/rerun.txt"},
        monochrome = true
)
public class FailedTestRunner extends AbstractTestNGCucumberTests {

    private static int attemptNumber = 0;

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        boolean retryEnabled = Boolean.parseBoolean(System.getProperty("Retry.Enabled", "false"));
        int maxRetry = Integer.parseInt(System.getProperty("Retry.Count", "2"));

        attemptNumber++;

        if (!retryEnabled || attemptNumber > maxRetry) {
            return new Object[0][0];
        }

        java.io.File rerunFile = new java.io.File("target/rerun.txt");
        if (!rerunFile.exists() || rerunFile.length() == 0) {
            return new Object[0][0];
        }

        // Backup existing result JSONs before retry overwrites them
        backupAllureResults();

        return super.scenarios();
    }

    private void backupAllureResults() {
        try {
            java.io.File resultsDir = new java.io.File("target/allure-results");
            for (java.io.File f : resultsDir.listFiles((d, n) -> n.endsWith("-result.json"))) {
                java.nio.file.Path backup = java.nio.file.Paths.get(
                    "target/allure-results/" + java.util.UUID.randomUUID() + "-result.json");
                java.nio.file.Files.copy(f.toPath(), backup,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
