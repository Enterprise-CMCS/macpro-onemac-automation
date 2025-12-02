package gov.cms.onemac.utils;

import gov.cms.onemac.pages.*;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public static DashboardPage getDashboardPage(WebDriver driver, UIElementUtils ui) {
        return new DashboardPage(driver, ui);
    }

    public static LoginPage getLoginPage(WebDriver driver, UIElementUtils ui) {
        return new LoginPage(driver, ui);
    }

    public static StateEarlyAlert getStateEarlyAlertPage(WebDriver driver, UIElementUtils ui) {
        return new StateEarlyAlert(driver, ui);
    }

    public static HomePage getHomePage(WebDriver driver, UIElementUtils ui) {
        return new HomePage(driver, ui);
    }

    public static SpaPage getSpaPage(WebDriver driver, UIElementUtils ui) {
        return new SpaPage(driver, ui);
    }

    public static WaiverPage getWaiverPage(WebDriver driver, UIElementUtils ui) {
        return new WaiverPage(driver, ui);
    }

    public static ViewFAQsPage getviewFAQsPage(WebDriver driver, UIElementUtils ui) {
        return new ViewFAQsPage(driver, ui);
    }
}
