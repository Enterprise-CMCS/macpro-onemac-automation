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

    public static StateEarlyAlertPage getStateEarlyAlertPage(WebDriver driver, UIElementUtils utils) {
        return new StateEarlyAlertPage(driver, utils);
    }

    public static SubmitMedicaidSpaPage getSubmitMedicaidSpaPage(WebDriver driver, UIElementUtils uiElementUtils) {
        return new SubmitMedicaidSpaPage(driver, uiElementUtils);
    }
}
