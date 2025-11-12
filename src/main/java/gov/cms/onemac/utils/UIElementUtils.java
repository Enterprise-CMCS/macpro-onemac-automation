package gov.cms.onemac.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class UIElementUtils {
    private WebDriver driver;
    private WebDriverWait wait;


    public UIElementUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public boolean isNotVisible(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            if (elements.isEmpty()) {
                return true;
            }
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void clickElement(By locator) {
        if (isClickable(locator)) {
            try {
                driver.findElement(locator).click();
            } catch (ElementClickInterceptedException e) {
                System.out.println("Click intercepted: " + e.getMessage());
            }
        } else {
            System.out.println("Element not clickable: " + locator);
        }

    }

    public void saveSpa(By locator) {
        clickElement(locator);
    }

    public void uploadFile(String filePath, By locator) {
        File file = new File(filePath);
        sendKeys(locator, file.getAbsolutePath());

    }

    public void sleepFor(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public boolean isVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isEnabled(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void sendKeys(By locator, String text) {
        driver.findElement(locator).sendKeys(text);
    }

    public void sendKeys(By locator, Keys keys) {
        driver.findElement(locator).sendKeys(keys);
    }

    public void sendKeysByActions(By locator, String textToSend) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(locator)).perform();
        driver.findElement(locator).sendKeys(textToSend);
    }

    public void selectFromDropdown(By locator, String selectionType, String selectionValue) {

        WebElement element = driver.findElement(locator);
        Select select = new Select(element);
        switch (selectionType.toLowerCase()) {
            case "text":
                select.selectByVisibleText(selectionValue);
                break;
            case "value":
                select.selectByValue(selectionValue);
                break;
            case "index":
                try {
                    int index = Integer.parseInt(selectionValue);
                    select.selectByIndex(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid index: " + selectionValue);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid selection type: " + selectionType +
                        ". Use 'text', 'value', or 'index'.");
        }
    }

    public void oneMACCalendarHandler(String date) {
        List<WebElement> list = driver.findElements(By.xpath("//tbody/tr/td/button"));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equalsIgnoreCase(date)) {
                Actions actions = new Actions(driver);
                actions.moveToElement(list.get(i)).click().perform();
                // list.get(i).click();
                break;
            }
        }
    }

    public void uploadFileAndCommit(By locator, String filePath) {
        // Step 1: trigger the upload (this replaces the original <input>)
        uploadFile(filePath, locator);
        // Step 2: re-locate the element AFTER upload to avoid stale reference
        WebElement el = wait.until(driver -> driver.findElement(locator));
        // Step 3: fire change + blur to commit validation
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }))", el);
        js.executeScript("arguments[0].dispatchEvent(new Event('blur', { bubbles: true }))", el);
    }

    public String getStateCode(String input) {
        if (input == null || !input.contains("-")) {
            return null;
        }

        return input.substring(0, input.indexOf('-'));
    }

    public String removeStateCode(String input) {
        if (input == null || !input.contains("-")) {
            return input;
        }

        return input.substring(input.indexOf('-') + 1);
    }

    public void javaScriptClicker(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].click();", element);
    }
}
