package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.pages.DashboardPage;
import gov.cms.onemac.pages.LoginPage;
import gov.cms.onemac.utils.AssertionUtil;
import gov.cms.onemac.utils.ConfigReader;
import gov.cms.onemac.utils.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardPageUIElementsTests extends BaseTest {
    private static final Logger logger = LogManager.getLogger();



    @Test
    public void verifySPAsTabVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isSPATabVisible(), "SPAs tab should be visible on dashboard.");
    }

  @Test
    public void verifySPAsTabVisibleForStateUser() {
      StateUser stateUser = createNewStateUser();
      stateUser.navigateToOneMac();
      stateUser.login();
        AssertionUtil.assertTrue(stateUser.isSPATabVisible(), "SPAs tab should be visible on dashboard.");
    }

    @Test
    public void verifySPAsTabClickableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isSPATabClickable(), "SPAs tab should be clickable.");
    }

    @Test
    public void verifySPAsTabClickableForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isSPATabClickable(), "SPAs tab should be visible on dashboard.");
    }

    @Test
    public void verifyNewSubmissionIsAvailableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isNewSubmissionAvailable(), "New Submission should be visible for state users.");
    }

    @Test
    public void verifyNewSubmissionIsNotAvailableForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isNewSubmissionNotAvailable(), "New Submission shouldn't be visible for cms users.");
    }

    @Test
    public void verifyWaiverTabIsVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isWaiverTabVisible(), "Waiver tab should be visible on dashboard for state users.");
    }

    @Test
    public void verifyWaiverTabIsVisibleForCMS() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isWaiverTabVisible(), "Waiver tab should be visible on dashboard for cms users.");
    }

   @Test
    public void verifyPageTitleIsCorrectForStateUser() {
       StateUser stateUser = createNewStateUser();
       stateUser.navigateToOneMac();
       stateUser.login();
       AssertionUtil.assertEquals(stateUser.getPageTitle(), "OneMAC","Page title should be 'OneMAC'.");

    }

    @Test
    public void verifyPageTitleIsCorrectForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertEquals(cmsUser.getPageTitle(), "OneMAC","Page title should be 'OneMAC'.");


    }

   @Test
    public void verifyHomeTabVisibleForCMSUser() {
       CMSUser cmsUser = createNewCMSUser();
       cmsUser.navigateToOneMac();
       cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isHomeTabVisible(),"Home tab should be visible for cms users.");
    }

    @Test
    public void verifyHomeTabVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isHomeTabVisible(),"Home tab should be visible for state users.");
    }


    @Test
    public void verifyDashboardVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isDashboardVisible(),"Dashboard should be visible for state users.");
    }

    @Test
    public void verifyDashboardVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isDashboardVisible(),"Dashboard tab should be visible for cms users.");
    }

    @Test
    public void verifyViewFAQsPageVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
       AssertionUtil.assertTrue(cmsUser.isViewFAQsPageVisible(),"View FAQs tab should be visible for cms users.");
    }

    @Test
    public void verifyViewFAQsPageVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isViewFAQsPageVisible(),"View FAQs tab should be visible for state users.");
    }


    @Test
    public void verifyHomeLinkClickableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isHomePageClickable(),"Home page should be clickable for state users.");
    }

    @Test
    public void verifyHomeLinkClickableForCmsUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isHomePageClickable(),"Home Page should be clickable for cms users.");
    }

    @Test
    public void verifyDashboardClickableForCmsUser() {
            CMSUser cmsUser = createNewCMSUser();
            cmsUser.navigateToOneMac();
            cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isDashboardClickable(),"Dashboard should be clickable for cms users.");
    }

    @Test
    public void verifyDashboardClickableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isDashboardClickable(),"Dashboard page should be clickable for state users.");
    }
}

