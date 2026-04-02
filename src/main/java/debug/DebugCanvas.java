package debug;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

public class DebugCanvas {
    public static void main(String[] args) throws Exception {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
            BrowserContext context = browser.newContext();
            final Page[] p = {context.newPage()};

            // Login
            p[0].navigate("https://mmw365.test.fence.cloud/login", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
            p[0].waitForLoadState(LoadState.NETWORKIDLE);
            p[0].getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("E-Mail Address")).fill("raj@vrize.com");
            p[0].getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("2026VRIZE");
            p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
            p[0].waitForLoadState(LoadState.NETWORKIDLE);

            // Create estimate
            p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create New")).click();
            p[0].getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Estimate")).click();
            p[0].waitForLoadState(LoadState.NETWORKIDLE);
            p[0].locator("#c_first_name").fill("Debug");
            p[0].locator("#c_last_name").fill("Test");
            p[0].evaluate("window.scrollBy(0,500)");
            p[0].waitForTimeout(1000);
            p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Use Map Coordinates +")).click();
            p[0].waitForTimeout(2000);
            p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Use for Location")).click();
            p[0].waitForLoadState(LoadState.NETWORKIDLE);
            p[0].evaluate("window.scrollTo(0,0)");

            // Create New Estimate - handle new tab
            try {
                Page np = p[0].context().waitForPage(() ->
                    p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create New Estimate")).click()
                );
                np.waitForLoadState(LoadState.NETWORKIDLE);
                p[0] = np;
            } catch (Exception e) { p[0].waitForTimeout(3000); }

            // Enter Siteplan - handle new tab
            p[0].waitForLoadState(LoadState.NETWORKIDLE);
            try {
                Page np = p[0].context().waitForPage(() ->
                    p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enter Siteplan")).click()
                );
                np.waitForLoadState(LoadState.NETWORKIDLE);
                p[0] = np;
            } catch (Exception e) {
                p[0].getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enter Siteplan")).click();
                p[0].waitForLoadState(LoadState.NETWORKIDLE);
            }

            // BlueGrid
            String href = (String) p[0].evaluate(
                "() => { const a = document.querySelector('a[href*=bluegrid]'); return a ? a.href : null; }"
            );
            System.out.println("BlueGrid URL: " + href);
            p[0].navigate(href, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
            p[0].waitForLoadState(LoadState.NETWORKIDLE);
            p[0].waitForTimeout(3000);

            // Capture console
            p[0].onConsoleMessage(msg -> System.out.println("BROWSER: " + msg.text()));

            // Inject listeners
            p[0].evaluate(
                "() => {" +
                "  const c = document.querySelector('canvas');" +
                "  if (!c) { console.log('NO CANVAS'); return; }" +
                "  console.log('Canvas: ' + c.width + 'x' + c.height);" +
                "  ['mousedown','mouseup','click','pointerdown','pointerup'].forEach(e => {" +
                "    c.addEventListener(e, ev => console.log(e+' x='+Math.round(ev.offsetX)+' y='+Math.round(ev.offsetY)));" +
                "  });" +
                "}"
            );

            Locator canvas = p[0].locator("canvas").first();
            com.microsoft.playwright.options.BoundingBox box = canvas.boundingBox();
            System.out.println("BBox: x=" + box.x + " y=" + box.y + " w=" + box.width + " h=" + box.height);

            // Test drag (most common for drawing tools)
            System.out.println("=== DRAG TEST ===");
            p[0].mouse().move(box.x + 100, box.y + 100);
            p[0].mouse().down();
            p[0].mouse().move(box.x + 200, box.y + 100, new Mouse.MoveOptions().setSteps(30));
            p[0].mouse().up();
            p[0].waitForTimeout(2000);

            System.out.println("=== TWO CLICKS TEST ===");
            p[0].mouse().click(box.x + 100, box.y + 200);
            p[0].waitForTimeout(800);
            p[0].mouse().click(box.x + 200, box.y + 200);
            p[0].waitForTimeout(2000);

            System.out.println("Keep browser open - observe which test drew lines...");
            Thread.sleep(20000);
            browser.close();
        }
    }
}
