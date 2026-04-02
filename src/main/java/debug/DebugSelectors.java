package debug;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import java.util.List;

public class DebugSelectors {
  public static void main(String[] args) throws Exception {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();

      // Login
      page.navigate("https://mmw365.test.fence.cloud/login");
      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("E-Mail Address")).fill("raj@vrize.com");
      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("2026VRIZE");
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
      page.waitForLoadState(LoadState.NETWORKIDLE);

      // Create Estimate
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create New")).click();
      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Estimate")).click();
      page.locator("#c_first_name").fill("Sanu");
      page.locator("#c_last_name").fill("test");
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create New Estimate")).click();
      page.waitForLoadState(LoadState.NETWORKIDLE);

      // Click first Create button visible
      List<String> createBtns = page.locator("button, input[type='submit'], input[type='button']").allTextContents();
      System.out.println("=== Buttons after Create New Estimate ===");
      for (String t : createBtns) { if (!t.trim().isEmpty()) System.out.println("  [" + t.trim() + "]"); }

      // Try clicking Create
      page.locator("button, input[type='submit']").filter(new Locator.FilterOptions().setHasText("Create")).first().click();
      page.waitForLoadState(LoadState.NETWORKIDLE);

      // Navigate to job page
      page.navigate("https://mmw365.test.fence.cloud/job/create");
      page.waitForLoadState(LoadState.NETWORKIDLE);

      // Click Use Map Coordinates
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Use Map Coordinates +")).click();
      page.waitForTimeout(3000);

      // Dump all buttons and links
      System.out.println("\n=== All buttons after Use Map Coordinates ===");
      List<String> buttons = page.locator("button").allTextContents();
      for (String t : buttons) { if (!t.trim().isEmpty()) System.out.println("  BUTTON: [" + t.trim() + "]"); }

      List<String> links = page.locator("a").allTextContents();
      for (String t : links) { if (!t.trim().isEmpty()) System.out.println("  LINK: [" + t.trim() + "]"); }

      // Dump all input values
      System.out.println("\n=== All inputs ===");
      List<ElementHandle> inputs = page.querySelectorAll("input[type='button'], input[type='submit']");
      for (ElementHandle el : inputs) {
        System.out.println("  INPUT: [" + el.getAttribute("value") + "] id=[" + el.getAttribute("id") + "] class=[" + el.getAttribute("class") + "]");
      }

      System.out.println("\nCurrent URL: " + page.url());
      Thread.sleep(5000);
      browser.close();
    }
  }
}
