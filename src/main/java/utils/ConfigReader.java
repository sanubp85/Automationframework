package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Prasanna Kumar
 */
public class ConfigReader {
    private static Properties properties;
    private static final String ENV = System.getProperty("env", "qa");

    static {
        loadProperties();
    }

    /**
     * Load properties based on environment
     * @author Prasanna Kumar
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            String configPath = "src/test/resources/config/" + ENV + "/" + ENV + ".properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file for environment: " + ENV);
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     * @author Prasanna Kumar
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get base URL from config
     * @return Base URL
     * @author Prasanna Kumar
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    /**
     * Get browser from config
     * @return Browser name
     * @author Prasanna Kumar
     */
    public static String getBrowser() {
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return envProps.getProperty("Browser", "chrome");
        } catch (IOException e) {
            return "chrome";
        }
    }

    /**
     * Get headless mode from config
     * @return true if headless, false otherwise
     * @author Prasanna Kumar
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    /**
     * Check if retry is enabled
     * @return true if retry enabled, false otherwise
     * @author Prasanna Kumar
     */
    public static boolean isRetryEnabled() {
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return Boolean.parseBoolean(envProps.getProperty("Retry.Enabled", "false"));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Check if screenshot on failure is enabled
     * @return true if enabled, false otherwise
     * @author Prasanna Kumar
     */
    public static boolean isScreenshotOnFailure() {
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return Boolean.parseBoolean(envProps.getProperty("Screenshot.OnFailure", "true"));
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * Check if screenshot on pass is enabled
     * @return true if enabled, false otherwise
     * @author Prasanna Kumar
     */
    public static boolean isScreenshotOnPass() {
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return Boolean.parseBoolean(envProps.getProperty("Screenshot.OnPass", "false"));
        } catch (IOException e) {
            return false;
        }
    }
}
