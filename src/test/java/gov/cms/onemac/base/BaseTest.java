package gov.cms.onemac.base;

import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.SeaUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.utils.DriverFactory;
import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private UIElementUtils utils;

    @BeforeMethod
    public void setUp() {
        createDriverSession();
    }

    protected void restartDriver() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception ignored) {
            }
        }
        createDriverSession();
    }

    private void createDriverSession() {
        WebDriver webDriver = DriverFactory.createDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.set(webDriver);
        utils = new UIElementUtils(getDriver(), 10);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public UIElementUtils getUtils() {
        return utils;
    }

    protected CMSUser createNewCMSUser() {
        return new CMSUser(getDriver(), getUtils());
    }

    protected StateUser createNewStateUser() {
        return new StateUser(getDriver(), getUtils());
    }

    protected SeaUser createNewSeaUser() {
        return new SeaUser(getDriver(),getUtils());
    }

/*    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = driver.get();
        if (d != null) {
            d.quit();
            driver.remove();
        }
    }*/
}
