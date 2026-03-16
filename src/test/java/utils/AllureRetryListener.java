package utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AllureRetryListener implements ITestListener, ISuiteListener {

    // Called before each <test> block in testng.xml starts
    @Override
    public void onStart(ISuite suite) {
    }

    // Called after each <test> block in testng.xml finishes
    @Override
    public void onFinish(ISuite suite) {
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Before each TestNG test method runs, rename existing result JSONs
        // so the upcoming run creates fresh files instead of overwriting
        renameExistingResultFiles();
    }

    private void renameExistingResultFiles() {
        try {
            File resultsDir = new File("target/allure-results");
            if (!resultsDir.exists()) return;

            File[] resultFiles = resultsDir.listFiles((d, n) -> n.endsWith("-result.json"));
            if (resultFiles == null) return;

            for (File f : resultFiles) {
                String newName = UUID.randomUUID().toString() + "-result.json";
                Files.move(f.toPath(),
                    Paths.get("target/allure-results/" + newName),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
