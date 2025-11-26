package gov.cms.onemac.pages;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {

    private WebDriver driver;
    private UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();
    final String filePath = "src/test/resources/testDocument.docx";

    // Locators
    private By waiversTab = By.xpath("//button[text()='Waivers']");
    private By spasTab = By.xpath("//button[text()='SPAs']");
    private By homeTab = By.cssSelector("a[data-testid='Home-d']");
    private By dashboardTab = By.cssSelector("a[data-testid='Dashboard-d']");
    private By userManagementTab = By.cssSelector("a[data-testid='Dashboard-d']");
    private By newSubmission = By.cssSelector("span[data-testid='new-sub-button']");
    private By homePageLink = By.cssSelector("a[data-testid='Home-d']");
    private By respondToRAI = By.linkText("Respond to Formal RAI");
    private By raiResponseWithdraw = By.linkText("Enable Formal RAI Response Withdraw");
    private By withdrawFormalRaiResponse = By.linkText("Withdraw Formal RAI Response");
    private By raiResponseLetter = By.cssSelector("input[data-testid='raiResponseLetter-upload']");
    private By submit = By.xpath("//button[text()='Submit']");

    private By withdrawalConfirmation = By.xpath("//button[text()=\"Yes, withdraw package\"]");
    private By confirmSubmission = By.xpath("//button[text()='Yes, Submit']");
    private By raiResponseSuccess = By.xpath("//h3[text()='RAI response submitted']");
    private By raiResponseWithdrawEnabled = By.xpath("//h3[text()='RAI response withdrawal enabled']");
    private By additionalInformation = By.tagName("textarea");
    private By withdrawFormalRaiResponseConfirmation = By.xpath("//button[text()='Yes, withdraw response']");
    private By withFormalRaiResponseRequestSubmitted = By.xpath("//h3[text()='Withdraw Formal RAI Response request has been submitted.']");
    private By withdrawRequestSuccessMessage = By.xpath("Withdraw package request has been submitted");
    private By withdrawPackage = By.linkText("Withdraw Package");
    private By uploadSubsequentDocuments = By.linkText("Upload Subsequent Documents");
    private By coverLetter = By.xpath("//label[text()=\"Cover Letter\"]/following-sibling::div/input");
    private By reasonForSubsequentDocuments = By.tagName("textArea");
    private By documentsSubmitted = By.xpath("//h3[text()=\"Documents submitted\"]");


    public DashboardPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SubmissionTypePage selectNewSubmission() {
        utils.javaScriptClicker(newSubmission);
        return new SubmissionTypePage(driver, utils);
    }

    public HomePage goToHomePage() {
        utils.isVisible(dashboardTab);
        utils.clickElement(homePageLink);
        return new HomePage(driver, utils);
    }

    public void respondToRAI() {
        utils.clickElement(respondToRAI);
        utils.uploadFile(filePath, raiResponseLetter);
        utils.clickElement(submit);
        utils.clickElement(confirmSubmission);
        utils.isVisible(raiResponseSuccess);
    }

    public void enableFormalRAIResponseWithdraw() {
        logger.info("Enabling Formal RAI Response Withdraw Status Started.");
        utils.clickElement(raiResponseWithdraw);
        utils.clickElement(submit);
        utils.isVisible(raiResponseWithdrawEnabled);
        logger.info("Enabling Formal RAI Response Withdraw Status Complete.");
    }

    public void withdrawFormalRaiResponse(String text) {
        logger.info("Formal RAI Response Withdrawal Started");
        utils.clickElement(withdrawFormalRaiResponse);
        utils.sendKeys(additionalInformation, text);
        utils.clickElement(submit);
        utils.clickElement(withdrawFormalRaiResponseConfirmation);
        utils.isVisible(withFormalRaiResponseRequestSubmitted);
        logger.info("Formal RAI Response Withdrawal Complete.");
    }


    public void clickPackage(String packageID) {
        utils.clickElement(By.linkText(packageID));
    }

    public void submitPackage(SpaPackage spaPackage, String effectiveDate) {
        SubmissionTypePage typePage = selectNewSubmission();
        typePage.selectSpaType()
                .selectMedicaidSpa()
                .allOtherMedicaidSpa()
                .enterSpaId(spaPackage.getPackageId())
                .pickEffectiveDate(effectiveDate)
                .uploadAttachments().submit();
        logger.info("Successfully Submitted Package: {} in OneMAC.", spaPackage.getPackageId());
    }

    public void submitWaiver(String waiverID, String date) {
        SubmissionTypePage typePage = selectNewSubmission();
        typePage.selectWaiverAction().
                selectWaiver1915bType().
                selectWaiver1915b4Authority().
                selectInitialWaiver().
                enterWaiverId(waiverID).
                pickEffectiveDate(date).
                uploadAttachment().
                submitPackage().
                isSubmitted();

    }


    public DashboardPage withdrawPackage(String additionalInfo) {
        utils.clickElement(withdrawPackage);
        utils.sendKeys(additionalInformation, additionalInfo);
        utils.clickElement(submit);
        utils.clickElement(withdrawalConfirmation);
        utils.isVisible(withdrawRequestSuccessMessage);
        return new DashboardPage(driver, utils);
    }

    public DashboardPage openPackage(SpaPackage spaPackage) {
        goToHomePage().
                searchSpaPackage(spaPackage.getPackageId()).
                clickPackage(spaPackage.getPackageId());
        return new DashboardPage(driver, utils);
    }
    public DashboardPage openWaiver(String waiver) {
        goToHomePage().
                searchWaiver(waiver).
                clickPackage(waiver);
        return new DashboardPage(driver, utils);
    }
    public void uploadSubsequentDocuments(String text) {
        utils.clickElement(uploadSubsequentDocuments);
        utils.uploadFileAndCommit(coverLetter, filePath);
        utils.sendKeys(reasonForSubsequentDocuments, text);
        utils.clickElement(submit);
        utils.clickElement(confirmSubmission);
        utils.isVisible(documentsSubmitted);
    }

    public boolean isDashboardVisible() {
        return utils.isVisible(dashboardTab);
    }

    public boolean isNewSubmissionAvailable() {
        return utils.isVisible(newSubmission);
    }

    public boolean isNewSubmissionNotAvailable() {
        return utils.isNotVisible(newSubmission);
    }
}
