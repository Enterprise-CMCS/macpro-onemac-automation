package gov.cms.onemac.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import gov.cms.onemac.utils.UIElementUtils;

public class LoginPage {
    private WebDriver driver;
    private UIElementUtils utils;

    // Locators
    private By signInLink = By.cssSelector("button[data-testid=\"sign-in-button-d\"]");
    private By usernameField = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"username\"]");
    private By passwordField = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"password\"]");
    private By loginButton = By.xpath("//div[text()=\"or\"]/parent::div//input[@name=\"signInSubmitButton\"]");

    public LoginPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils=utils;
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


    public void login(String username, String password) {
        clickSignInLink();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

}
