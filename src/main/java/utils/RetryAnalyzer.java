package utils;

import io.qameta.allure.Allure;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    public static final ThreadLocal<Integer> CURRENT_RETRY = new ThreadLocal<>();
    public static final ThreadLocal<Integer> MAX_RETRY = new ThreadLocal<>();

    @Override
    public boolean retry(ITestResult result) {
        boolean retryEnabled = Boolean.parseBoolean(
            System.getProperty("Retry.Enabled", String.valueOf(ConfigReader.isRetryEnabled())));
        int maxRetryCount = Integer.parseInt(
            System.getProperty("Retry.Count", "2"));

        if (retryEnabled && retryCount < maxRetryCount) {
            retryCount++;
            CURRENT_RETRY.set(retryCount);
            MAX_RETRY.set(maxRetryCount);
            return true;
        }
        CURRENT_RETRY.remove();
        MAX_RETRY.remove();
        return false;
    }
}
