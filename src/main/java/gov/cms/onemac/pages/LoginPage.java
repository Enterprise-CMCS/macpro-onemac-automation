package gov.cms.onemac.pages;

import gov.cms.onemac.utils.ConfigReader;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class LoginPage {

    private WebDriver driver;
    private UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();

    // Locators
    private By signInLink = By.cssSelector("button[data-testid=\"sign-in-button-d\"]");
    private By usernameField = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"username\"]");
    private By passwordField = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"password\"]");
    private By loginButton = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"signInSubmitButton\"]");


    public LoginPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }


    public void enterUsername(String username) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(usernameField)).perform();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void clickSignInLink() {
        driver.findElement(signInLink).click();
    }


    public DashboardPage login(String username, String password) {
        clickSignInLink();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver, utils);
    }

    public DashboardPage loginAsStateUser() {
        logger.info("State User Sign In Started...");
        clickSignInLink();
        enterUsername(ConfigReader.getUsername("state"));
        enterPassword(ConfigReader.getPassword("state"));
        clickLogin();
        logger.info("State User Sign In Complete.");
        return new DashboardPage(driver, utils);
    }

    public DashboardPage loginAsCmsUser() {
        logger.info("Signing in to OneMAC as a cms user...");
        clickSignInLink();
        enterUsername(ConfigReader.getUsername("cms"));
        enterPassword(ConfigReader.getPassword("cms"));
        clickLogin();
        logger.info("Signing in to OneMAC as a cms user was successful.");
        return new DashboardPage(driver, utils);
    }
}
