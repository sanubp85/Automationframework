package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * @author Prasanna Kumar
 */
public class BasePage {
    protected Page page;

    /**
     * Constructor to initialize BasePage with Playwright Page instance
     * @param page Playwright Page object
     * @author Prasanna Kumar
     */
    public BasePage(Page page) {
        this.page = page;
    }

    /**
     * Navigate to the specified URL
     * @param url URL to navigate to
     * @author Prasanna Kumar
     */
    public void navigateTo(String url) {
        page.navigate(url);
    }

    /**
     * Wait for element to be visible and click on it
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void click(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.click(selector);
    }

    /**
     * Wait for element to be visible and fill text into it
     * @param selector CSS selector of the element
     * @param text Text to fill
     * @author Prasanna Kumar
     */
    public void fill(String selector, String text) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.fill(selector, text);
    }

    /**
     * Wait for element to be visible and get its text content
     * @param selector CSS selector of the element
     * @return Text content of the element
     * @author Prasanna Kumar
     */
    public String getText(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.textContent(selector);
    }

    /**
     * Wait for element to be visible and clear its text
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void clearText(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.fill(selector, "");
    }

    /**
     * Get text content from multiple elements
     * @param selector CSS selector of the elements
     * @return Array of text content from all matching elements
     * @author Prasanna Kumar
     */
    public String[] getTextFromElements(String selector) {
        page.waitForSelector(selector);
        return page.locator(selector).allTextContents().toArray(new String[0]);
    }

    /**
     * Wait for element to be present and check if it is visible
     * @param selector CSS selector of the element
     * @return true if element is visible, false otherwise
     * @author Prasanna Kumar
     */
    public boolean isVisible(String selector) {
        page.waitForSelector(selector);
        return page.isVisible(selector);
    }

    /**
     * Wait for element to be present in DOM
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    /**
     * Wait for element to be visible
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void waitForVisible(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    /**
     * Wait for element to be hidden
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void waitForHidden(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    /**
     * Wait for specified milliseconds
     * @param milliseconds Time to wait in milliseconds
     * @author Prasanna Kumar
     */
    public void waitForTimeout(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    /**
     * Wait for page load state to complete
     * @author Prasanna Kumar
     */
    public void waitForLoadState() {
        page.waitForLoadState();
    }

    /**
     * Assert element is visible
     * @param selector CSS selector of the element
     * @author Prasanna Kumar
     */
    public void assertVisible(String selector) {
        assert isVisible(selector) : "Element not visible: " + selector;
    }

    /**
     * Assert element text equals expected text
     * @param selector CSS selector of the element
     * @param expectedText Expected text content
     * @author Prasanna Kumar
     */
    public void assertTextEquals(String selector, String expectedText) {
        String actualText = getText(selector);
        assert actualText.equals(expectedText) : "Expected: " + expectedText + ", Actual: " + actualText;
    }

    /**
     * Assert element text contains expected text
     * @param selector CSS selector of the element
     * @param expectedText Expected text to be contained
     * @author Prasanna Kumar
     */
    public void assertTextContains(String selector, String expectedText) {
        String actualText = getText(selector);
        assert actualText.contains(expectedText) : "Text '" + expectedText + "' not found in: " + actualText;
    }

    /**
     * Assert page URL equals expected URL
     * @param expectedUrl Expected URL
     * @author Prasanna Kumar
     */
    public void assertUrlEquals(String expectedUrl) {
        String actualUrl = page.url();
        assert actualUrl.equals(expectedUrl) : "Expected URL: " + expectedUrl + ", Actual URL: " + actualUrl;
    }

    /**
     * Assert page title equals expected title
     * @param expectedTitle Expected page title
     * @author Prasanna Kumar
     */
    public void assertTitleEquals(String expectedTitle) {
        String actualTitle = page.title();
        assert actualTitle.equals(expectedTitle) : "Expected Title: " + expectedTitle + ", Actual Title: " + actualTitle;
    }
}
