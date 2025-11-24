package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.pages.DashboardPage;
import gov.cms.onemac.pages.LoginPage;
import gov.cms.onemac.utils.ConfigReader;
import gov.cms.onemac.utils.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardPageUIElementsTests extends BaseTest {
    private static final Logger logger = LogManager.getLogger();

    @BeforeMethod
    public void navigateToOneMAC(){
        getDriver().get(ConfigReader.get("oneMACDev"));
    }

    @Test
    public void verifySPAsTabVisibleForCMSUser() {
        logger.info("Starting login test for CMS user");
        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        logger.info("Login test completed successfully");
        DashboardPage dashboardPage = PageFactory.getDashboardPage(getDriver(), getUtils());
       // Assert.assertTrue(dashboardPage.isSPAsTabVisible(), "SPAs tab not visible on dashboard.");
    }

/*    @Test
    public void verifySPAsTabVisibleForStateUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isSPAsTabVisible(), "SPAs tab not visible on dashboard.");
    }

    @Test
    public void verifySPAsTabClickableForStateUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboardPage = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboardPage.isSPAsClickable(), "SPAs tab not clickable.");
    }

    @Test
    public void verifySPAsTabClickableForCMSUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboardPage = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboardPage.isSPAsClickable(), "SPAs tab not clickable.");
    }

    @Test
    public void verifyNewSubmissionIsAvailableForStateUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isNewSubmissionAvailable(), "New Submission isn't available.");
    }

    @Test
    public void verifyNewSubmissionIsNotAvailableForCMSUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isNewSubmissionNotAvailable(), "New Submission is available.");
    }

    @Test
    public void verifyWaiverTabIsVisibleForStateUser() {

        LoginPage loginPage = PageFactory.getLoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isWaiverTabVisible(), "Waiver tab not visible on dashboard.");
    }

    @Test
    public void verifyWaiverTabIsVisibleForCMS() {

        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isWaiverTabVisible(), "Waiver tab not visible on dashboard.");
    }

    @Test
    public void verifyPageTitleIsCorrectForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        // Assert.assertEquals(dashboard.getPageTitle(), "OneMAC");

    }

    @Test
    public void verifyPageTitleIsCorrectForCMSUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        //  Assert.assertEquals(dashboard.getPageTitle(), "OneMAC");

    }

    @Test
    public void verifyHomeTabVisibleForCMSUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isHomeTabVisible());
    }

    @Test
    public void verifyHomeTabVisibleForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isHomeTabVisible());
    }


    @Test
    public void verifyDashboardVisibleForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isDashboardVisible());
    }

    @Test
    public void verifyDashboardVisibleForCMSUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isDashboardVisible());
    }

    @Test
    public void verifyUserManagementTabIsVisibleForCMSUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isUserManagementVisible());
    }

    @Test
    public void verifyUserManagementTabIsVisibleForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isUserManagementVisible());
    }

    @Test
    public void verifyHomeLinkClickableForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isHomeLinkClickable());
    }

    @Test
    public void verifyHomeLinkClickableForCmsUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isHomeLinkClickable());
    }

    @Test
    public void verifyDashboardClickableForCmsUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isDashboardClickable());
    }

    @Test
    public void verifyDashboardClickableForStateUser() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        DashboardPage dashboard = PageFactory.getDashboardPage(getDriver(), getUtils());
        Assert.assertTrue(dashboard.isDashboardClickable());
    }*/
}
