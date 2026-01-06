package gov.cms.onemac.pages;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.PageFactory;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

public class DashboardPage {

    private WebDriver driver;
    private UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();
    final String filePath = "src/test/resources/testDocument.docx";

    // Locators
    private static final By DASHBOARD_GRID =
            By.xpath("//tbody/tr");
    private static final By WAIVERS_TAB
            = By.xpath("//button[text()='Waivers']");
    private static final By SPA_TAB = By.xpath("//button[text()='SPAs']");
    private By homeTab = By.cssSelector("a[data-testid='Home-d']");
    private By dashboardTab = By.cssSelector("a[data-testid='Dashboard-d']");
    private By userManagementTab = By.cssSelector("a[data-testid='Dashboard-d']");
    private By newSubmission = By.cssSelector("span[data-testid='new-sub-button']");
    private By homePageLink = By.cssSelector("a[data-testid='Home-d']");
    private By respondToRAI = By.linkText("Respond to Formal RAI");
    private By raiResponseWithdraw = By.linkText("Enable Formal RAI Response Withdraw");
    private By withdrawFormalRaiResponse = By.linkText("Withdraw Formal RAI Response");
    private By raiResponseLetter = By.cssSelector("input[data-testid='raiResponseLetter-upload']");
    private By waiverRAIResponseLetter = By.xpath("//label[text()=\"Waiver RAI Response Letter\"]/following-sibling::div/input");
    private By submit = By.xpath("//button[text()='Submit']");
    private static final By ACTION_TYPE =
            By.xpath("//div[text()=\"Action Type\"]");
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
    private By revisedStatePlanLanguage = By.xpath("//label[text()=\"Revised Amended State Plan Language\"]/following-sibling::div/input");
    private By officialRAIResponse = By.xpath("//label[text()=\"Official RAI Response\"]/following-sibling::div/input");
    private By reasonForSubsequentDocuments = By.tagName("textArea");
    private By documentsSubmitted = By.xpath("//h3[text()=\"Documents submitted\"]");
    private By columns = By.cssSelector("button[data-testid=\"columns-menu-btn\"]");
    private By filter = By.cssSelector("button[aria-label=\"Open filter panel\"]");

    private final By SEARCH_FIELD =
            By.id("search-input");
    private static final By STATUS =
            By.xpath("//h2[text()=\"Status\"]");

    public DashboardPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void selectColumn(String columnName) {
        utils.waitForThreeDotsLoadingToDisappear();
        utils.clickElement(columns);
        driver.findElement(By.xpath("//span[text()=\"" + columnName + "\"]/ancestor::li")).click();
    }

    public void verifyExport() throws Exception {
        utils.waitForThreeDotsLoadingToDisappear();
        By exportButton = By.xpath("//button[.//span[text()='Export']]");
        utils.clickElement(exportButton);
        File downloadedCsv = waitForCsvDownload();
        Assert.assertTrue(downloadedCsv.exists(), "CSV file was not downloaded");
        //Validate CSV content
        validateCsvContent(downloadedCsv);
        //Delete file
        deleteExistingCsvFiles();

    }

    private void deleteExistingCsvFiles() {
        String projectRoot = System.getProperty("user.dir");
        File rootDir = new File(projectRoot);
        File[] files = rootDir.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private void validateCsvContent(File csvFile) throws IOException {
        List<String> lines = Files.readAllLines(csvFile.toPath());
        Assert.assertTrue(lines.size() > 1, "CSV file has no data rows");

        // Header validation
        String[] actualHeaders = lines.get(0).split(",");

        List<String> expectedHeaders = List.of(
                "SPA ID",
                "State",
                "Authority",
                "Status",
                "Initial Submission",
                "Latest Package Activity",
                "Formal RAI Response",
                "Submitted By"
        );

        for (int i = 0; i < expectedHeaders.size(); i++) {
            String actualHeader = normalizeHeader(
                    actualHeaders[i].replace("\"", "")
            );

            Assert.assertEquals(
                    actualHeader,
                    expectedHeaders.get(i),
                    "Header mismatch at column " + i
            );
        }
        // Sample data validation (first data row)
        String[] firstRow = lines.get(1).split(",");
        System.out.println(firstRow[0]);
        Assert.assertFalse(firstRow[0].isEmpty(), "SPA ID is empty in CSV");
    }

    private String normalizeHeader(String value) {
        if (value == null) {
            return null;
        }
        return value
                .replace("\uFEFF", "")   // BOM
                .replace("\u200B", "")   // zero-width space
                .replace("\u00A0", " ")  // non-breaking space
                .trim();
    }


    private File waitForCsvDownload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String projectRoot = System.getProperty("user.dir");
        return wait.until(d -> {
            File rootDir = new File(projectRoot);
            File[] files = rootDir.listFiles((dir, name) ->
                    name.endsWith(".csv") && !name.endsWith(".crdownload"));
            return (files != null && files.length > 0) ? files[0] : null;
        });
    }


    public boolean filterByState(String state) {
        utils.waitForThreeDotsLoadingToDisappear();
        utils.clickElement(filter);
        driver.findElement(By.xpath("//button[text()='State']")).click();
        utils.clickElement(By.xpath("//div[text()='Select...']"));
        driver.findElement(By.cssSelector("input[aria-label='State']")).sendKeys(state);

        if (driver.findElement(By.xpath("//div[@role='listbox']/div")).getText().contains(state)) {
            driver.findElement(By.xpath("//div[@role='listbox']/div")).click();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(By.xpath("//tbody/tr[1]/td[3]"), state));

        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));

        for (WebElement row : rows) {
            String text = row.findElement(By.xpath("./td[3]")).getText().trim();

            if (!text.equalsIgnoreCase(state)) {
                System.out.println("Filter is not working. Found: " + text);
                return false;
            }
        }
        return true;
    }


    public void selectNewSubmission() {
        utils.javaScriptClicker(newSubmission);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public HomePage goToHomePage() {
        utils.isVisible(dashboardTab);
        utils.clickElement(homePageLink);
        return new HomePage(driver, utils);
    }

    public void respondToRAI(String authority) {
        utils.clickElement(respondToRAI);
        if (authority.contains("CHIP")) {
            utils.uploadFile(filePath, revisedStatePlanLanguage);
            utils.uploadFile(filePath, officialRAIResponse);
        } else {
            utils.uploadFile(filePath, raiResponseLetter);
        }
        utils.clickElement(submit);
        utils.clickElement(confirmSubmission);
        utils.isVisible(raiResponseSuccess);
    }

    public void respondToAppendixKWaiverRAI() {
        utils.clickElement(respondToRAI);
        utils.uploadFile(filePath, waiverRAIResponseLetter);
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

    public String submitWaiver1915cAppendixK(String amendmentTitle, String date) {
        return PageFactory.getWaiverPage(driver, utils).submitWaiver1915cAppendixK(amendmentTitle, date);
    }


    public SpaPackage submitNewStateAmendmentSPA(String state, String authority) {
        return PageFactory.getSpaPage(driver, utils).submitNewStateAmendmentSPA(state, authority);
    }

    public SpaPackage submitNewStateAmendmentCHIPSPA(String state, String authority) {
        return PageFactory.getSpaPage(driver, utils).submitNewStateAmendmentCHIPSPA(state, authority);
    }

    public String createFFSSelectiveContractingInitialWaiver() {
        return PageFactory.getWaiverPage(driver, utils).createFFSSelectiveContractingInitialWaiver();
    }


    public DashboardPage withdrawPackage(String additionalInfo) {
        utils.clickElement(withdrawPackage);
        utils.sendKeys(additionalInformation, additionalInfo);
        utils.clickElement(submit);
        utils.clickElement(withdrawalConfirmation);
        utils.isVisible(withdrawRequestSuccessMessage);
        return new DashboardPage(driver, utils);
    }

    public DashboardPage openSpaPackage(SpaPackage spaPackage) {
        utils.waitForThreeDotsLoadingToDisappear();
        utils.clickElement(dashboardTab);
        utils.clearInput(SEARCH_FIELD);
        utils.sendKeys(SEARCH_FIELD, spaPackage.getPackageId());
        utils.waitForSingleRecordAndClick();
        utils.isVisible(STATUS);
        return new DashboardPage(driver, utils);
    }

    public DashboardPage openWaiverPackage(String waiver) {
        utils.clickElement(dashboardTab);
        utils.clickElement(WAIVERS_TAB);
        utils.isVisible(ACTION_TYPE);
        utils.clearInput(SEARCH_FIELD);
        utils.sendKeys(SEARCH_FIELD, waiver);
        utils.waitForNumberOfElementsToBe(DASHBOARD_GRID, 1);
        clickPackage(waiver);
        return new DashboardPage(driver, utils);
    }

    public void searchWaiver(String waiverID) {
        utils.clickElement(WAIVERS_TAB);
        utils.isVisible(ACTION_TYPE);
        utils.sendKeys(SEARCH_FIELD, waiverID);
    }

    public void searchForSPA(String spa) {
        utils.sendKeys(SEARCH_FIELD, spa);
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

    public boolean isWaiverTabVisible() {
        return utils.isVisible(WAIVERS_TAB);
    }

    public boolean isNewSubmissionAvailable() {
        return utils.isVisible(newSubmission);
    }

    public boolean isNewSubmissionNotAvailable() {
        return utils.isNotVisible(newSubmission);
    }

    public boolean isSPATabVisible() {
        return utils.isVisible(SPA_TAB);
    }

    public boolean isPackageFound(String waiverID) {
        utils.waitForNumberOfElementsToBe(DASHBOARD_GRID, 1);
        return driver.findElement(By.xpath("//a[text()=" + "\"" + waiverID + "\"" + "]")).isDisplayed();
    }

    public boolean columnDisplayed(String columnName) {
        return driver.findElement(By.xpath("//th/div[text()=\"" + columnName + "\"]")).isDisplayed();
    }

    public boolean columnHidden(String columnName) {
        Duration originalWait = driver.manage().timeouts().getImplicitWaitTimeout();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        if (driver.findElements(By.xpath("//th/div[text()=\"" + columnName + "\"]")).isEmpty()) {
            driver.manage().timeouts().implicitlyWait(originalWait);
            return true;
        }
        driver.manage().timeouts().implicitlyWait(originalWait);
        return false;
    }

    public boolean isSPATabClickable() {
        return utils.isClickable(SPA_TAB);
    }

    public boolean isHomeTabVisible() {
        return utils.isVisible(homeTab);
    }

    public boolean isHomePageClickable() {
        return utils.isClickable(homeTab);
    }

    public boolean isDashboardClickable() {
        return utils.isClickable(dashboardTab);
    }


}
