package utils;

import com.microsoft.playwright.*;

/**
 * @author Prasanna Kumar
 */
public class BrowserManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    /**
     * Initialize browser based on configuration
     * @author Prasanna Kumar
     */
    public static void initBrowser() {
        playwright = Playwright.create();
        String browserName = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();
        
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        
        switch (browserName.toLowerCase()) {
            case "chrome":
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "edge":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(headless).setChannel("msedge"));
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browserName);
        }
        
        context = browser.newContext();
        page = context.newPage();
    }

    /**
     * Get current page instance
     * @return Page instance
     * @author Prasanna Kumar
     */
    public static Page getPage() {
        return page;
    }

    /**
     * Close browser and cleanup resources
     * @author Prasanna Kumar
     */
    public static void closeBrowser() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
