package utils;

import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureRetryListener implements ITestListener {
    
    @Override
    public void onTestFailure(ITestResult result) {
        if (result.wasRetried()) {
            Allure.getLifecycle().updateTestCase(testResult -> {
                testResult.setStatusDetails(testResult.getStatusDetails().setMessage("Test failed and will be retried"));
            });
        }
    }
}
