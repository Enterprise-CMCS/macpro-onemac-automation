package gov.cms.onemac.flows;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.pages.DashboardPage;
import gov.cms.onemac.pages.SubmitMedicaidSpaPage;
import gov.cms.onemac.utils.PageFactory;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class StateUser {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private By raiWithdrawalRequested = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Formal RAI Response - Withdrawal Requested']");
    private By withdrawFormalRaiResponse = By.linkText("Withdraw Formal RAI Response");
    private By submitted = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Submitted']");
    private By respondToRAI = By.linkText("Respond to Formal RAI");
    private By raiIssued = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='RAI Issued']");
    private By packageWithdrawn = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Package Withdrawn']");
    private By underReview = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Under Review']");
    private By uploadSubsequentDocuments = By.linkText("Upload Subsequent Documents");
    private By withdrawPackage = By.linkText("Withdraw Package");
    private By withdrawalRequest = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Withdrawal Requested']");
    private final int TIME_OUT = 5;

    private static final Logger logger = LogManager.getLogger(StateUser.class);

    public StateUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public DashboardPage login() {
        return PageFactory.getLoginPage(driver, utils).loginAsStateUser();
    }

    public void navigateToOneMac() {
        logger.info("Navigating To OneMAC...");
        driver.get(utils.getOneMACEnv());
    }

    public void submitPackage(SpaPackage spa, String effDate) {
        PageFactory.getDashboardPage(driver, utils).submitPackage(spa, effDate);
    }

    public SubmitMedicaidSpaPage verifySpaSubmitted(SpaPackage spaPackage) {
        return PageFactory.getSubmitMedicaidSpaPage(driver, utils).isSpaSubmitted(spaPackage);
    }

    public DashboardPage withdrawPackage(SpaPackage spaPackage, String additionalInfo) {
        navigateToOneMac();
        return PageFactory.getDashboardPage(driver, utils)
                .openPackage(spaPackage)
                .withdrawPackage(additionalInfo);
    }

    public void openPackage(SpaPackage spaPackage) {
        PageFactory.getDashboardPage(driver, utils)
                .openPackage(spaPackage);
    }

    public boolean isPackageStatusUnderReview() {
        return utils.refreshUntilVisible(underReview, TIME_OUT);
    }

    public boolean isUploadSubsequentDocumentsLinkPresent() {
        return utils.refreshUntilVisible(uploadSubsequentDocuments, TIME_OUT);
    }

    public boolean isWithdrawPackageLinkPresent() {
        return utils.refreshUntilVisible(withdrawPackage, TIME_OUT);
    }

    public boolean isWithdrawalStatusPresent() {
        return utils.refreshUntilVisible(withdrawalRequest, TIME_OUT);
    }

    public boolean isPackageStatusWithdrawn() {
        return utils.refreshUntilVisible(packageWithdrawn, TIME_OUT);
    }

    public boolean isRaiIssued() {
        return utils.refreshUntilVisible(raiIssued, TIME_OUT);
    }

    public void respondToRAI() {
        PageFactory.getDashboardPage(driver, utils).respondToRAI();
    }

    public boolean isFormalRaiLinkVisible() {
        return utils.refreshUntilVisible(respondToRAI, TIME_OUT);
    }

    public boolean isPackageStatusSubmitted() {
        return utils.refreshUntilVisible(submitted, TIME_OUT);
    }

    public boolean isWithdrawFormalRAIResponseLinkVisible() {
        return utils.refreshUntilVisible(withdrawFormalRaiResponse, TIME_OUT);
    }

    public void withdrawFormalRaiResponse(String text) {
        PageFactory.getDashboardPage(driver, utils).withdrawFormalRaiResponse(text);
    }

    public boolean isStatusUpdatedToFormalRAIResponseWithdrawalRequested() {
        logger.info("RAI Response - Withdrawal Requested Status Check Completed for State User.");
        boolean status = utils.refreshUntilVisible(raiWithdrawalRequested, TIME_OUT);
        logger.info("RAI Response - Withdrawal Requested Status Check Completed for State User.");
        return status;
    }
}
