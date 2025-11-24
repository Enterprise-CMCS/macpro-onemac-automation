package gov.cms.onemac.utils;

import gov.cms.onemac.pages.*;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public static DashboardPage getDashboardPage(WebDriver driver, UIElementUtils utils) {
        return new DashboardPage(driver, utils);
    }

    public static LoginPage getLoginPage(WebDriver driver, UIElementUtils utils) {
        return new LoginPage(driver, utils);
    }

    public static StateEarlyAlert getStateEarlyAlertPage(WebDriver driver, UIElementUtils utils) {
        return new StateEarlyAlert(driver, utils);
    }

    public static SubmitMedicaidSpaPage getSubmitMedicaidSpaPage(WebDriver driver, UIElementUtils uiElementUtils) {
        return new SubmitMedicaidSpaPage(driver, uiElementUtils);
    }

    public static HomePage getHomePage(WebDriver driver, UIElementUtils uiElementUtils) {
        return new HomePage(driver, uiElementUtils);
    }

}
