package gov.cms.onemac.flows;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.pages.DashboardPage;
import gov.cms.onemac.utils.PageFactory;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSUser {
    private By raiWithdrawalRequested = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Formal RAI Response - Withdrawal Requested']");
    private By raiResponseWithdraw = By.linkText("Enable Formal RAI Response Withdraw");
    private By pendingRAI = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Pending - RAI']");
    private By pending = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Pending']");
    private By intakeNeeded = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Submitted - Intake Needed']");
    private By packageWithdrawn = By.xpath("//dl[@aria-labelledby='package-status-heading']/dt[text()='Package Withdrawn']");
    private static final Logger logger = LogManager.getLogger();
    private WebDriver driver;
    private UIElementUtils utils;

    private final int TIME_OUT = 5;

    public CMSUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public DashboardPage login() {
        return PageFactory.getLoginPage(driver, utils).loginAsCmsUser();
    }

    public void navigateToOneMac() {
        logger.info("Navigating To OneMAC...");
        driver.get(utils.getOneMACEnv());
    }

    public void openPackage(SpaPackage spaPackage) {
        PageFactory.getDashboardPage(driver, utils).
                openPackage(spaPackage);

    }

    public boolean isPackageStatusPending() {
        return utils.refreshUntilVisible(pending, TIME_OUT);
    }

    public boolean isPackageStatusSubmittedIntakeNeeded() {
        return utils.refreshUntilVisible(intakeNeeded, TIME_OUT);
    }

    public boolean isPackageStatusWithdrawn() {
        return utils.refreshUntilVisible(packageWithdrawn, TIME_OUT);
    }

    public boolean isStatusPendingRAI() {
        return utils.refreshUntilVisible(pendingRAI, TIME_OUT);
    }

    public boolean isStatusSubmittedIntakeNeeded() {
        return utils.refreshUntilVisible(intakeNeeded, TIME_OUT);
    }

    public boolean isEnableFormalRAIResponseLinkVisible() {
        return utils.refreshUntilVisible(raiResponseWithdraw, TIME_OUT);
    }

    public void enableRAIResponseWithdraw() {
      PageFactory.getDashboardPage(driver,utils).enableFormalRAIResponseWithdraw();
    }

    public boolean isStatusUpdatedToFormalRAIResponseWithdrawalRequested() {
        logger.info("Checking that package status displays RAI Response - Withdrawal Requested for CMS User...");
        boolean status = utils.refreshUntilVisible(raiWithdrawalRequested, TIME_OUT);
        logger.info("Checking that package status displays RAI Response - Withdrawal Requested for CMS User is Complete.");
        return status;
    }

}
