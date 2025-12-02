package gov.cms.onemac.flows;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.pages.DashboardPage;
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
    private By unsubmitted = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Unsubmitted']");
    private By uploadSubsequentDocuments = By.linkText("Upload Subsequent Documents");
    private By withdrawPackage = By.linkText("Withdraw Package");
    private By withdrawalRequest = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Withdrawal Requested']");
    private By coverLetter = By.xpath("//tbody/tr/td[text()=\"Cover Letter\"]/parent::tr/td[2]/button[text()=\"testDocument.docx\"]");
    private By statusApproved = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Approved']");
    private By statusDisapproved = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Disapproved']");
    private By statusTerminated = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Terminated']");
    private final int TIME_OUT = 5;

    private static final Logger logger = LogManager.getLogger(StateUser.class);

    public StateUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public DashboardPage login() {
        return PageFactory.getLoginPage(driver, utils).loginAsStateUser();
    }

    public String submitWaiver1915cAppendixK(String amendmentTitle, String proposedEffectiveDate) {
       return PageFactory.getDashboardPage(driver, utils).submitWaiver1915cAppendixK(amendmentTitle, proposedEffectiveDate);
    }

    public void navigateToOneMac() {
        logger.info("Navigating To OneMAC...");
        driver.get(utils.getOneMACEnv());
    }

    public SpaPackage submitNewStateAmendmentSPA(String state, String authority) {
        return  PageFactory.getDashboardPage(driver, utils).submitNewStateAmendmentSPA(state, authority);
    }
    public String createFFSSelectiveContractingInitialWaiver(){
       return PageFactory.getDashboardPage(driver, utils).createFFSSelectiveContractingInitialWaiver();
    }

    public DashboardPage withdrawPackage(SpaPackage spaPackage, String additionalInfo) {
        navigateToOneMac();
        return PageFactory.getDashboardPage(driver, utils)
                .openSpaPackage(spaPackage)
                .withdrawPackage(additionalInfo);
    }

    public void openPackage(SpaPackage spaPackage) {
        PageFactory.getDashboardPage(driver, utils)
                .openSpaPackage(spaPackage);
    }

    public void openWaiverPackage(String waiver) {
        PageFactory.getDashboardPage(driver, utils)
                .openWaiverPackage(waiver);
    }

    public void uploadSubsequentDocuments(String text) {
        PageFactory.getDashboardPage(driver, utils).uploadSubsequentDocuments(text);
    }

    public boolean isUploadSubsequentDocumentsLinkNotVisible() {
        return utils.refreshUntilInvisible(uploadSubsequentDocuments, TIME_OUT);
    }

    public boolean isPackageStatusUnderReview() {
        return utils.refreshUntilVisible(underReview, TIME_OUT);
    }
    public boolean isPackageStatusUnsubmitted() {
        return utils.refreshUntilVisible(unsubmitted, TIME_OUT);
    }

    public boolean isPackageStatusApproved() {
        return utils.refreshUntilVisible(statusApproved, TIME_OUT);
    }

    public boolean isPackageStatusDisapproved() {
        return utils.refreshUntilVisible(statusDisapproved, TIME_OUT);
    }

    public boolean isPackageStatusTerminated() {
        return utils.refreshUntilVisible(statusTerminated, TIME_OUT);
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
        logger.info("RAI Response - Withdrawal Requested Status Check started for State User...");
        boolean status = utils.refreshUntilVisible(raiWithdrawalRequested, TIME_OUT);
        logger.info("RAI Response - Withdrawal Requested Status Check Completed for State User.");
        return status;
    }

    public boolean isCoverLetterPresent() {
        return utils.isVisible(coverLetter);
    }

    public boolean isSPATabVisible() {
      return PageFactory.getDashboardPage(driver,utils).isSPATabVisible();
    }

    public boolean isSPATabClickable() {
        return PageFactory.getDashboardPage(driver,utils).isSPATabClickable();
    }
    public boolean isNewSubmissionAvailable() {
        return PageFactory.getDashboardPage(driver,utils).isNewSubmissionAvailable();
    }
    public boolean isWaiverTabVisible() {
        return PageFactory.getDashboardPage(driver,utils).isWaiverTabVisible();
    }
    public String getPageTitle(){
        return PageFactory.getDashboardPage(driver,utils).getPageTitle();
    }
    public boolean isHomeTabVisible(){
        return PageFactory.getDashboardPage(driver,utils).isHomeTabVisible();
    }
    public boolean isDashboardVisible(){
        return PageFactory.getDashboardPage(driver,utils).isDashboardVisible();
    }
    public boolean isHomePageClickable(){
        return PageFactory.getDashboardPage(driver,utils).isHomePageClickable();
    }
    public boolean isDashboardClickable(){
        return PageFactory.getDashboardPage(driver,utils).isDashboardClickable();
    }
    public boolean isViewFAQsPageVisible(){
        return PageFactory.getviewFAQsPage(driver,utils).isViewFAQsTabVisible();
    }
}
