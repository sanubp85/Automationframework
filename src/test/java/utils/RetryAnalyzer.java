package utils;

import io.qameta.allure.Allure;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            result.setAttribute("RETRY_COUNT", retryCount);
            Allure.step("Retry attempt #" + retryCount + " of " + maxRetryCount);
            return true;
        }
        return false;
    }
}
