package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.models.BasePackage;
import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.models.WaiverPackage;
import gov.cms.onemac.pages.DashboardPage;
import gov.cms.onemac.pages.LoginPage;
import gov.cms.onemac.pages.StateEarlyAlertPage;
import gov.cms.onemac.utils.*;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;


public class E2E extends BaseTest {


    private static final Logger logger = LogHelper.getLogger();


    @Test
    public void submitSpaInOneMAC() {
        //Create SPA in OneMAC
        getDriver().get(ConfigReader.get("oneMACDev"));
        String spaId = SpaIdGenerator.nextSpa("MD", "25");
        LoginPage loginPage = new LoginPage(getDriver(), getUtils());
        logger.info("Signing in to OneMAC as a state user...");
        loginPage.login(ConfigReader.getUsername("state"), ConfigReader.getPassword("state"));
        logger.info("Signing in to OneMAC as a state user was successful");
        DashboardPage dashboardPage = PageFactory.getDashboardPage(getDriver(), getUtils());
        dashboardPage.goToSubmissionType()
                .selectSpaType().
                selectMedicaidSpa().
                allOtherMedicaidSpa().
                enterSpaId(spaId).
                pickEffectiveDate("12").
                uploadAttachments().
                submit();
        //check if SPA submitted before writing package to the Excel sheet
        if (PageFactory.getSubmitMedicaidSpaPage(getDriver(), getUtils()).isSubmitted()) {
            logger.info("Successfully submitted Spa: " + spaId + " in OneMAC.");
            ExcelPackageTracker.appendNewPackage("SPA", "MD", "Medicaid SPA", "", spaId, "Submitted", "");
        }
        getDriver().get(ConfigReader.get("seaDEV"));
        StateEarlyAlertPage stateEarlyAlertPage = PageFactory.getStateEarlyAlertPage(getDriver(), getUtils());
        stateEarlyAlertPage.login(ConfigReader.getSEAUsername("seaUsername"), ConfigReader.getSEAPassword("seaPassword"));
        // SpaPackage spaPackage = ExcelPackageSelector.selectSubmittedPA("MD", "Medicaid SPA");
        stateEarlyAlertPage.createPackage(spaId, "11/12/2025", "11/12/2025");
        logger.info("Successfully submitted Spa: " + spaId + " in SEATool.");
    }

    @Test
    public void submitSpaInSEATool() {
      /*  String spa = "MD-25-9078";
        //CreateSPA in SEATool and write package to excel sheet
        getDriver().get(ConfigReader.get("seaDEV"));
        StateEarlyAlertPage stateEarlyAlertPage = PageFactory.getStateEarlyAlertPage(getDriver(), getUtils());
        stateEarlyAlertPage.login(ConfigReader.getSEAUsername("seaUsername"), ConfigReader.getSEAPassword("seaPassword"));
        // SpaPackage spaPackage = ExcelPackageSelector.selectSubmittedPA("MD", "Medicaid SPA");
        stateEarlyAlertPage.createPackage(spa, "11/12/2025", "11/12/2025");
        logger.info("Successfully submitted Spa: " + spa + " in SEATool.");*/
        //  ExcelPackageTracker.removePackageById(spaPackage.getPackageId());
    }

    @Test
    public void generatePackages() {
       // PackagePoolGenerator.generate(List.of("MD","CO","AL","NY"));

        //Generate a SPA package
       // SpaPackage spaPackage = SpaGenerator.createSpa("MD", "Medicaid SPA");


        //  System.out.println(spaPackage.getPackageId());


        //Generate Renewal based on approved initial
      /*  WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("CO", "1915(b)");

        List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );*/

        //Generate Renewal based on approved renewal
        /*WaiverPackage parent = ExcelPackageSelector.selectUnapprovedRenewal("MD", "1915(b)");

        List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );*/

        //Generate Temporary Extension based on approved initial
       /* WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());


        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );*/

        //Generate Temporary Extension based on approved renewal
       /*  WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );*/

        //Generate amendment based on an approved initial
        /*WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );*/

        //Generate amendment based on an approved renewal
     /*   WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );*/
    }
}







