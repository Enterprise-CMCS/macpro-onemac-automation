package gov.cms.onemac.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;


public class DashboardPage {
    private WebDriver driver;
    private UIElementUtils utils;

    // Locators
    private By waiversTab = By.xpath("//button[text()=\"Waivers\"]");

    private By spasTab = By.xpath("//button[text()=\"SPAs\"]");

    private By homeTab = By.cssSelector("a[data-testid=\"Home-d\"]");

    private By dashboardTab = By.cssSelector("a[data-testid=\"Dashboard-d\"]");

    private By userManagementTab = By.cssSelector("a[data-testid=\"Dashboard-d\"]");

    private By newSubmission = By.cssSelector("span[data-testid=\"new-sub-button\"]");




    public DashboardPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SubmissionTypePage goToSubmissionType() {
        clickNewSubmission();
        return new SubmissionTypePage(driver, utils);
    }

    public void clickNewSubmission() {
        utils.javaScriptClicker(newSubmission);
    }



    public void clickOnWaiversTab() {
        driver.findElement(waiversTab).click();
    }

    public boolean isWaiverTabVisible() {
        return utils.isVisible(waiversTab);
    }

    public boolean isHomeTabVisible() {
        return utils.isVisible(homeTab);
    }

    public boolean isSPAsTabVisible() {
        return utils.isVisible(spasTab);
    }


    public boolean isDashboardVisible() {
        return utils.isVisible(dashboardTab);
    }

    public boolean isUserManagementVisible() {
        return utils.isVisible(userManagementTab);
    }

    public boolean isHomeLinkClickable() {
        return utils.isClickable(homeTab);
    }

    public boolean isDashboardClickable() {
        return utils.isClickable(dashboardTab);
    }

    public boolean isSPAsClickable() {
        return utils.isClickable(dashboardTab);
    }

    public boolean isNewSubmissionAvailable() {
        return utils.isVisible(newSubmission);
    }

    public boolean isNewSubmissionNotAvailable() {
        return utils.isNotVisible(newSubmission);
    }
}
