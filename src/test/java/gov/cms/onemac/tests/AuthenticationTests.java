package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.StateUser;
import org.testng.annotations.Test;


public class AuthenticationTests extends BaseTest {


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

}
