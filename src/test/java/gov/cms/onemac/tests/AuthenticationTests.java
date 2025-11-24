package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.pages.LoginPage;
import gov.cms.onemac.utils.ConfigReader;
import org.testng.annotations.Test;


public class AuthenticationTests extends BaseTest {


   @Test
    public void stateUserCanLogin() {
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(

                ConfigReader.getUsername("state"),
                ConfigReader.getPassword("state")
        );
        // Add assertions
    }

/*    @Test
    public void cmsUserCanLogin() {

        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        loginPage.login(
                ConfigReader.getUsername("cms"),
                ConfigReader.getPassword("cms")
        );
        // Add assertions
    }*/

}
