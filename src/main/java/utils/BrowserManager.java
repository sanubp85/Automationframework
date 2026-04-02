package utils;

import com.microsoft.playwright.*;
import java.nio.file.Paths;

/**
 * @author Prasanna Kumar
 */
public class BrowserManager {
    private static final String STORAGE_STATE_PATH = "auth/storageState.json";
    private static final ThreadLocal<Playwright> playwrightTL = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserTL = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextTL = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageTL = new ThreadLocal<>();

    public static void initBrowser() {
        Playwright playwright = Playwright.create();
        playwrightTL.set(playwright);

        String browserName = System.getProperty("browser", ConfigReader.getBrowser());
        boolean headless = ConfigReader.isHeadless();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        Browser browser;
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

        browserTL.set(browser);
        BrowserContext context = browser.newContext();
        contextTL.set(context);
        contextTL.set(context);
        pageTL.set(context.newPage());
    }

    public static void saveStorageState() {
        try {
            java.nio.file.Files.createDirectories(Paths.get("auth"));
            contextTL.get().storageState(new BrowserContext.StorageStateOptions()
                .setPath(Paths.get(STORAGE_STATE_PATH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Page getPage() {
        return pageTL.get();
    }

    public static void setPage(Page page) {
        pageTL.set(page);
    }

    public static void closeBrowser() {
        try { if (contextTL.get() != null) contextTL.get().close(); } catch (Exception ignored) {}
        try { if (browserTL.get() != null) browserTL.get().close(); } catch (Exception ignored) {}
        try { if (playwrightTL.get() != null) playwrightTL.get().close(); } catch (Exception ignored) {}
        pageTL.remove();
        contextTL.remove();
        browserTL.remove();
        playwrightTL.remove();
    }
}
