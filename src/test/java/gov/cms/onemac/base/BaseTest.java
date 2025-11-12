package gov.cms.onemac.base;

import gov.cms.onemac.utils.ConfigReader;
import gov.cms.onemac.utils.DriverFactory;
import gov.cms.onemac.utils.LogHelper;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;


import java.time.Duration;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private UIElementUtils utils;

    private static final Logger logger = LogHelper.getLogger();

    @BeforeMethod
    public void setUp() {
        logger.info("Initializing WebDriver setup...");
        WebDriver webDriver = DriverFactory.createDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.set(webDriver);
        utils = new UIElementUtils(getDriver(), 10);
    }

   /* @AfterMethod
    public void tearDown() {
        logger.info("Closing browser...");
        if (driver.get() != null) {
            driver.get().quit();
        }

    }*/

    public static WebDriver getDriver() {
        return driver.get();
    }



    public UIElementUtils getUtils() {
        return utils;
    }

}

