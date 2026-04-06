package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Sanu B P
 */
public class ConfigReader {
    private static Properties properties;
    private static final String ENV = System.getProperty("env", "qa");

    static {
        loadProperties();
    }

    /**
     * Load properties based on environment
     * @author Sanu B P
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
     * @author Sanu B P
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get base URL from config
     * @return Base URL
     * @author Sanu B P
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getPeople360Url() {
        return properties.getProperty("base.people360.url");
    }

    /**
     * Get browser from config
     * @return Browser name
     * @author Sanu B P
     */
    public static String getBrowser() {
        // allow overriding browser via system property: -Dbrowser=chrome
        String browserFromSys = System.getProperty("browser");
        if (browserFromSys == null || browserFromSys.isEmpty()) {
            browserFromSys = System.getProperty("Browser");
        }
        if (browserFromSys != null && !browserFromSys.isEmpty()) {
            return browserFromSys;
        }

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
     * @author Sanu B P
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    /**
     * Check if retry is enabled
     * @return true if retry enabled, false otherwise
     * @author Sanu B P
     */
    public static boolean isRetryEnabled() {
        String sys = System.getProperty("Retry.Enabled");
        if (sys != null && !sys.trim().isEmpty()) return Boolean.parseBoolean(sys.trim());
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return Boolean.parseBoolean(envProps.getProperty("Retry.Enabled", "false").trim());
        } catch (IOException e) {
            return false;
        }
    }

    public static int getRetryCount() {
        String sys = System.getProperty("Retry.Count");
        if (sys != null && !sys.trim().isEmpty()) return Integer.parseInt(sys.trim());
        try {
            Properties envProps = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/environment.properties");
            envProps.load(fis);
            fis.close();
            return Integer.parseInt(envProps.getProperty("Retry.Count", "2").trim());
        } catch (IOException e) {
            return 2;
        }
    }

    /**
     * Check if screenshot on failure is enabled
     * @return true if enabled, false otherwise
     * @author Sanu B P
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
     * @author Sanu B P
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
