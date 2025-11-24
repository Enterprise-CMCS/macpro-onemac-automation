package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private WebDriver driver;
    private UIElementUtils utils;

    // Locators
    private By searchSPA = By.cssSelector("input[aria-label=\"Search for text\"]");
    private By searchButton = By.xpath("//button[text()=\"Search\"]");


    public HomePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public DashboardPage searchSpaPackage(String spaID) {
        utils.sendKeys(searchSPA, spaID);
        utils.clickElement(searchButton);
        return new DashboardPage(driver, utils);
    }



}
