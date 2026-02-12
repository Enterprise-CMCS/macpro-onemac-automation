package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.models.WaiverPackage;
import gov.cms.onemac.utils.AssertionUtil;
import gov.cms.onemac.utils.ExcelPackageSelector;
import gov.cms.onemac.utils.PageFactory;
import org.testng.annotations.Test;

public class DashboardPageUIElementsTests extends BaseTest {

    @Test
    public void searchForWaiver() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        WaiverPackage waiverPackage = ExcelPackageSelector.selectWaiver("MD", "1915(c)", "Amendment", "Submitted");
        cmsUser.searchForWaiver(waiverPackage.getPackageId());
        AssertionUtil.assertTrue(PageFactory.getDashboardPage(getDriver(), getUtils()).isPackageFound(waiverPackage.getPackageId()), "Package should be displayed in the Dashboard.");
    }

    @Test
    public void searchForSPA() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        SpaPackage spa = ExcelPackageSelector.selectSpa("MD", "Medicaid SPA", "Submitted");
        cmsUser.searchForSPA(spa.getPackageId());
        AssertionUtil.assertTrue(PageFactory.getDashboardPage(getDriver(), getUtils()).isPackageFound(spa.getPackageId()), "Package should be displayed in the Dashboard.");
    }

    @Test
    public void verifyShowColumn() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        cmsUser.selectColumn("Final Disposition");
        AssertionUtil.assertTrue(PageFactory.getDashboardPage(getDriver(), getUtils()).columnDisplayed("Final Disposition"), "Final Disposition should be visible.");
    }

    @Test
    public void verifyHideColumn() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        cmsUser.selectColumn("Authority");
        AssertionUtil.assertTrue(PageFactory.getDashboardPage(getDriver(), getUtils()).columnHidden("Authority"), "Authority should be hidden.");
    }


    @Test
    public void stateUserCanLogin() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        // Add assertion
    }

    @Test
    public void cmsUserCanLogin() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        // Add assertion
    }

    @Test
    public void verifyFilters() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.filterByState("TX"),"All states have been validated");
    }

    @Test
    public void verifyExport() throws Exception {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        cmsUser.verifyExport();
    }

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
        AssertionUtil.assertEquals(stateUser.getPageTitle(), "OneMAC", "Page title should be 'OneMAC'.");

    }

    @Test
    public void verifyPageTitleIsCorrectForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertEquals(cmsUser.getPageTitle(), "OneMAC", "Page title should be 'OneMAC'.");


    }

    @Test
    public void verifyHomeTabVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isHomeTabVisible(), "Home tab should be visible for cms users.");
    }

    @Test
    public void verifyHomeTabVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isHomeTabVisible(), "Home tab should be visible for state users.");
    }


    @Test
    public void verifyDashboardVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isDashboardVisible(), "Dashboard should be visible for state users.");
    }

    @Test
    public void verifyDashboardVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isDashboardVisible(), "Dashboard tab should be visible for cms users.");
    }

    @Test
    public void verifyViewFAQsPageVisibleForCMSUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isViewFAQsPageVisible(), "View FAQs tab should be visible for cms users.");
    }

    @Test
    public void verifyViewFAQsPageVisibleForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isViewFAQsPageVisible(), "View FAQs tab should be visible for state users.");
    }


    @Test
    public void verifyHomeLinkClickableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isHomePageClickable(), "Home page should be clickable for state users.");
    }

    @Test
    public void verifyHomeLinkClickableForCmsUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isHomePageClickable(), "Home Page should be clickable for cms users.");
    }

    @Test
    public void verifyDashboardClickableForCmsUser() {
        CMSUser cmsUser = createNewCMSUser();
        cmsUser.navigateToOneMac();
        cmsUser.login();
        AssertionUtil.assertTrue(cmsUser.isDashboardClickable(), "Dashboard should be clickable for cms users.");
    }

    @Test
    public void verifyDashboardClickableForStateUser() {
        StateUser stateUser = createNewStateUser();
        stateUser.navigateToOneMac();
        stateUser.login();
        AssertionUtil.assertTrue(stateUser.isDashboardClickable(), "Dashboard page should be clickable for state users.");
    }
}

