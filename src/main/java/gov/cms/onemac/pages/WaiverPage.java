package gov.cms.onemac.pages;

import gov.cms.onemac.models.WaiverPackage;
import gov.cms.onemac.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WaiverPage {

    private static final Logger logger = LogManager.getLogger();

    private final WebDriver driver;
    private final UIElementUtils ui;
    private By submit = By.cssSelector("button[data-testid=\"submit-action-form\"]");
    private By packageSubmitted = By.xpath("//h3[text()=\"Package submitted\"]");
    private By initialWaiver = By.xpath("//h2[text()=\"1915(b)(4) FFS Selective Contracting New Initial Waiver\"]/ancestor::a");
    private By waiverId = By.id("id");
    private By months = By.name("months");
    private By waiverCalendar = By.cssSelector("button[data-testid=\"proposedEffectiveDate-datepicker\"]");
    private By waiverApplication = By.xpath("//label[text()=\"1915(b)(4) FFS Selective Contracting (Streamlined) Waiver Application Pre-print\"]/following-sibling::div/input");
    final String FILE_PATH = "src/test/resources/testDocument.docx";
    private By waiver1915b4FFSLink = By.xpath("//h2[text()=\"1915(b)(4) FFS Selective Contracting Waivers\"]/ancestor::a");

    private static final By MONTHS =
            By.name("months");
    private static final By WAIVER_CALENDAR =
            By.cssSelector("button[data-testid=\"proposedEffectiveDate-datepicker\"]");
    private static final By WAIVER_1915B_TILE =
            By.xpath("//h2[text()='1915(b) Waiver Actions']/ancestor::a");

    private static final By WAIVER_1915C_APPENDIX_K_TILE =
            By.xpath("//h2[text()='1915(c) Appendix K Amendment']/ancestor::a");

    private static final By AMENDMENT_TITLE =
            By.tagName("textarea");

    private static final By WAIVER_AMENDMENT_NUMBER =
            By.id("id");

    private static final By WAIVER_ACTION_TILE =
            By.xpath("//h2[text()='Waiver Action']/ancestor::a");

    private static final By WAIVER_1915C_APPENDIX_K_TEMPLATE =
            By.xpath("//label[text()=\"1915(c) Appendix K Amendment Waiver Template\"]/following-sibling::div/input");

    private static final By SUBMIT =
            By.cssSelector("button[data-testid=\"submit-action-form\"]");

    private static final By PACKAGE_SUBMITTED =
            By.xpath("//h3[text()=\"Package submitted\"]");

    public WaiverPage(WebDriver driver, UIElementUtils ui) {
        this.driver = driver;
        this.ui = ui;
    }

    public void selectInitialWaiver() {
        ui.clickElement(initialWaiver);
    }

    public void enterWaiverId(String spaId) {
        ui.sendKeys(waiverId, spaId);
    }

    public void pickEffectiveDate(String date) {
        ui.javaScriptClicker(waiverCalendar);
        ui.selectFromDropdown(months, "text", date);
        ui.oneMACCalendarHandler(date);
    }

    public void uploadAttachment() {
        ui.uploadFileAndCommit(waiverApplication, FILE_PATH);
    }

    public void submitPackage() {
        ui.clickElement(submit);
    }

    public boolean isSubmitted() {
        return ui.isVisible(packageSubmitted);
    }

    public String createFFSSelectiveContractingInitialWaiver(){
        driver.get(ui.getOneMACEnv());
        PageFactory.getLoginPage(driver,ui).loginAsStateUser();
        PageFactory.getDashboardPage(driver,ui).selectNewSubmission();
        ui.clickElement(WAIVER_ACTION_TILE);
        openWaiver1915bTypePage();
        selectWaiver1915b4Authority();
        selectInitialWaiver();
        String waiverId = WaiverIdGenerator.nextInitial("MD");
        ExcelPackageTracker.appendNewPackage(
                "Waiver", "MD", "1915(b)", "Initial", waiverId, "", ""
        );
        enterWaiverId(waiverId);
        pickEffectiveDate(ui.getProposedEffectiveDate());
        uploadAttachment();
        submitPackage();
        if (isSubmitted()) {
            logger.info("Successfully submitted Spa: {} in OneMAC.", waiverId);
            ExcelPackageTracker.updateStatus(waiverId, "Submitted");
        }
        return waiverId;
    }

    public void openWaiver1915bTypePage() {
        ui.clickElement(WAIVER_1915B_TILE);
    }

    public void selectWaiver1915b4Authority() {
        ui.clickElement(waiver1915b4FFSLink);

    }

    public String submitWaiver1915cAppendixK(String amendmentTitle, String proposedEffectiveDate) {
        logger.info("Navigating To OneMAC...");
        driver.get(ui.getOneMACEnv());
        PageFactory.getLoginPage(driver,ui).loginAsStateUser();
        PageFactory.getDashboardPage(driver,ui).selectNewSubmission();
        ui.clickElement(WAIVER_ACTION_TILE);
        ui.clickElement(WAIVER_1915C_APPENDIX_K_TILE);
        ui.sendKeys(AMENDMENT_TITLE, amendmentTitle);
        WaiverPackage parent = ExcelPackageSelector.selectWaiver("MD", "1915(c)", "Initial", "Approved");
        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());
        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );
        ui.sendKeys(WAIVER_AMENDMENT_NUMBER, amendment);
        ui.javaScriptClicker(WAIVER_CALENDAR);
        ui.selectFromDropdown(MONTHS, "text", proposedEffectiveDate);
        ui.oneMACCalendarHandler(proposedEffectiveDate);
        ui.uploadFileAndCommit(WAIVER_1915C_APPENDIX_K_TEMPLATE, FILE_PATH);
        ui.clickElement(SUBMIT);
        if (isSubmitted()) {
            logger.info("Successfully submitted Waiver: {} in OneMAC.", amendment);
            ExcelPackageTracker.updateStatus(amendment, "Submitted");
        }
        return amendment;
    }
}
