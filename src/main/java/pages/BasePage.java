package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * @author Sanu B P
 */
public class BasePage {
    protected Page page;

    /**
     * Constructor to initialize BasePage with Playwright Page instance
     * @param page Playwright Page object
     * @author Sanu B P
     */
    public BasePage(Page page) {
        this.page = page;
    }

    /**
     * Navigate to the specified URL
     * @param url URL to navigate to
     * @author Sanu B P
     */
    public void navigateTo(String url) {
        page.navigate(url);
    }

    /**
     * Wait for element to be visible and click on it
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void click(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.click(selector);
    }

    /**
     * Wait for element to be visible and fill text into it
     * @param selector CSS selector of the element
     * @param text Text to fill
     * @author Sanu B P
     */
    public void fill(String selector, String text) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.fill(selector, text);
    }

    /**
     * Wait for element to be visible and get its text content
     * @param selector CSS selector of the element
     * @return Text content of the element
     * @author Sanu B P
     */
    public String getText(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.textContent(selector);
    }

    /**
     * Wait for element to be visible and clear its text
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void clearText(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.fill(selector, "");
    }

    /**
     * Get text content from multiple elements
     * @param selector CSS selector of the elements
     * @return Array of text content from all matching elements
     * @author Sanu B P
     */
    public String[] getTextFromElements(String selector) {
        page.waitForSelector(selector);
        return page.locator(selector).allTextContents().toArray(new String[0]);
    }

    /**
     * Wait for element to be present and check if it is visible
     * @param selector CSS selector of the element
     * @return true if element is visible, false otherwise
     * @author Sanu B P
     */
    public boolean isVisible(String selector) {
        page.waitForSelector(selector);
        return page.isVisible(selector);
    }

    /**
     * Wait for element to be present in DOM
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    /**
     * Wait for element to be visible
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void waitForVisible(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    /**
     * Wait for element to be hidden
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void waitForHidden(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    /**
     * Wait for specified milliseconds
     * @param milliseconds Time to wait in milliseconds
     * @author Sanu B P
     */
    public void waitForTimeout(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    /**
     * Wait for page load state to complete
     * @author Sanu B P
     */
    public void waitForLoadState() {
        page.waitForLoadState();
    }

    /**
     * Assert element is visible
     * @param selector CSS selector of the element
     * @author Sanu B P
     */
    public void assertVisible(String selector) {
        assert isVisible(selector) : "Element not visible: " + selector;
    }

    /**
     * Assert element text equals expected text
     * @param selector CSS selector of the element
     * @param expectedText Expected text content
     * @author Sanu B P
     */
    public void assertTextEquals(String selector, String expectedText) {
        String actualText = getText(selector);
        assert actualText.equals(expectedText) : "Expected: " + expectedText + ", Actual: " + actualText;
    }

    /**
     * Assert element text contains expected text
     * @param selector CSS selector of the element
     * @param expectedText Expected text to be contained
     * @author Sanu B P
     */
    public void assertTextContains(String selector, String expectedText) {
        String actualText = getText(selector);
        assert actualText.contains(expectedText) : "Text '" + expectedText + "' not found in: " + actualText;
    }

    /**
     * Assert page URL equals expected URL
     * @param expectedUrl Expected URL
     * @author Sanu B P
     */
    public void assertUrlEquals(String expectedUrl) {
        String actualUrl = page.url();
        assert actualUrl.equals(expectedUrl) : "Expected URL: " + expectedUrl + ", Actual URL: " + actualUrl;
    }

    /**
     * Assert page title equals expected title
     * @param expectedTitle Expected page title
     * @author Sanu B P
     */
    public void assertTitleEquals(String expectedTitle) {
        String actualTitle = page.title();
        assert actualTitle.equals(expectedTitle) : "Expected Title: " + expectedTitle + ", Actual Title: " + actualTitle;
    }
}
