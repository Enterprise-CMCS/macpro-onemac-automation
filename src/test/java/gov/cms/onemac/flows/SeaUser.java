package gov.cms.onemac.flows;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.PageFactory;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class SeaUser {
    private static final Logger logger = LogManager.getLogger(SeaUser.class);
    private WebDriver driver;
    private UIElementUtils utils;

    public SeaUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void login() {
        PageFactory.getStateEarlyAlertPage(driver, utils).login();
    }

    public void navigateToSEATool() {
        PageFactory.getStateEarlyAlertPage(driver, utils).navigateToSEATool();
    }

    public void requestRai(SpaPackage spa, String initialSubDate, String proposedEffDate, String raiRequestDate, String spaAuthority, String svcType, String svcSubType) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .requestRAI(spa.getPackageId(), initialSubDate, proposedEffDate, raiRequestDate, spaAuthority, svcType, svcSubType);
    }

    public void requestRaiForChipSPA(String spaID, String raiRequestDate) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .requestRaiForChipSpa(spaID, raiRequestDate);
    }

    public void createSPAPackage(SpaPackage spa, String initialDate, String proposedEffectiveDate) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .createSPAPackage(spa.getPackageId(), initialDate, proposedEffectiveDate);
    }

    public void createWaiverPackage(String waiver, String initialDate, String proposedEffectiveDate) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .createWaiverPackage(waiver, initialDate, proposedEffectiveDate);
    }

    public void updatePackageStatus(SpaPackage spa, String status) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .updatePackageStatus(spa.getPackageId(), status);
    }

    public void updatePackageStatus(String waiver, String status) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .updatePackageStatus(waiver, status);
    }

    public void addResponseReceivedDate(SpaPackage spa, String responseDate) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .addRaiResponseReceivedDate(spa.getPackageId(), responseDate);
    }

    public void addRaiResponseWithdrawnDate(SpaPackage spa, String raiResponseWithdrawalDate) {
        logger.info("Navigating Back to SEATool To Add RAI Response Withdrawn Date.");
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .addRaiResponseWithdrawnDate(spa.getPackageId(), raiResponseWithdrawalDate);
        logger.info("Successfully Added RAI Response Withdrawn Date as {} In SEATool.", raiResponseWithdrawalDate);
    }

    public void markPackageApproved(SpaPackage spa) {
        PageFactory.getStateEarlyAlertPage(driver, utils)
                .markPackageApproved(spa.getPackageId());
    }
}
