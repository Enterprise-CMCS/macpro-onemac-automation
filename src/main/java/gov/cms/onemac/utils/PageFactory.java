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

    public static SubmitMedicaidSpaPage getSubmitMedicaidSpaPage(WebDriver driver, UIElementUtils ui) {
        return new SubmitMedicaidSpaPage(driver, ui);
    }

    public static HomePage getHomePage(WebDriver driver, UIElementUtils ui) {
        return new HomePage(driver, ui);
    }
    public static SpaPage getSpaTypePage(WebDriver driver, UIElementUtils ui) {
        return new SpaPage(driver, ui);
    }

}
