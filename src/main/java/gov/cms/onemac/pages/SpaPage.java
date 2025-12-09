package gov.cms.onemac.pages;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.ExcelPackageTracker;
import gov.cms.onemac.utils.PageFactory;
import gov.cms.onemac.utils.SpaGenerator;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SpaPage {
    final String filePath = "src/test/resources/testDocument.docx";
    private static final Logger logger = LogManager.getLogger();
    private WebDriver driver;
    private UIElementUtils ui;
    private static final By SPA_TITLE =
            By.xpath("//h2[text()='State Plan Amendment (SPA)']/ancestor::a");
    private By medicaidSPA = By.xpath("//p[text()=\"Submit a new Medicaid State Plan Amendment\"]/ancestor::a");
    private By allOtherMedicaidSpa = By.xpath("//h2[text()=\"All Other Medicaid SPA Submissions\"]/ancestor::a");
    private By spaIDField = By.id("spa-id");

    private By medicaidSpaCalendar = By.cssSelector("button[data-testid=\"proposedEffectiveDate-datepicker\"]");

    private By CMS179Form = By.xpath("//label[text()=\"CMS-179 Form\"]/following-sibling::div/input");

    private By spaPages = By.xpath("//label[text()=\"SPA Pages\"]/following-sibling::div/input");

    private By saveSpa = By.cssSelector("button[data-testid=\"submit-action-form\"]");

    private By packageSubmitted = By.xpath("//h3[text()=\"Package submitted\"]");

    private By addInfo = By.tagName("textarea");

    private By months = By.name("months");

    public SpaPage(WebDriver driver, UIElementUtils ui) {
        this.driver = driver;
        this.ui = ui;
    }
public SpaPackage submitNewStateAmendmentSPA(String state, String authority){
    logger.info("Navigating To OneMAC...");
    driver.get(ui.getOneMACEnv());
    PageFactory.getLoginPage(driver,ui).loginAsStateUser();
    PageFactory.getDashboardPage(driver,ui).selectNewSubmission();
    logger.info("Submitting new {}...", authority);
    ui.clickElement(SPA_TITLE);
    ui.clickElement(medicaidSPA);
    ui.clickElement(allOtherMedicaidSpa);
    SpaPackage spa = SpaGenerator.createSpa(state, authority);
    ui.sendKeys(spaIDField, spa.getPackageId());
    ui.javaScriptClicker(medicaidSpaCalendar);
    ui.selectFromDropdown(months, "text", ui.getProposedEffectiveDate());
    ui.oneMACCalendarHandler(ui.getProposedEffectiveDate());
    ui.uploadFileAndCommit(CMS179Form, filePath);
    ui.uploadFileAndCommit(spaPages, filePath);
    ui.waitForElementToBeStableAndEnabled(saveSpa,300,10);
    ui.saveSpa(saveSpa);
    if (isSubmitted()) {
        logger.info("Successfully submitted Spa: {} in OneMAC.", spa.getPackageId());
        ExcelPackageTracker.updateStatus(spa.getPackageId(), "Submitted");
    }
    PageFactory.getDashboardPage(driver, ui);
    return spa;
}


   /* public SubmitMedicaidSpaPage allOtherMedicaidSpa() {
        ui.clickElement(allOtherMedicaidSpa);
        return new SubmitMedicaidSpaPage(driver, ui);
    }*/
  /*  public SubmitMedicaidSpaPage enterSpaId(String spaId) {
        ui.sendKeys(spaIDField, spaId);
        return this;
    }*/

   /* public SubmitMedicaidSpaPage pickEffectiveDate(String date) {
        ui.javaScriptClicker(medicaidSpaCalendar);
        ui.selectFromDropdown(months, "text", date);
        ui.oneMACCalendarHandler(date);
        return this;
    }
*/

    /*public void submit() {
        ui.saveSpa(saveSpa);
    }*/

    public boolean isSubmitted() {
        return ui.isVisible(packageSubmitted);
    }

    /*public SubmitMedicaidSpaPage uploadAttachments() {
        ui.uploadFileAndCommit(CMS179Form, filePath);
        ui.uploadFileAndCommit(spaPages, filePath);
        return this;
    }*/

    /*public SubmitMedicaidSpaPage isSpaSubmitted(SpaPackage spaPackage) {
        if (isSubmitted()) {
            logger.info("Successfully submitted Spa: {} in OneMAC.", spaPackage.getPackageId());
            ExcelPackageTracker.updateStatus(spaPackage.getPackageId(), "Submitted");
        }
        return this;
    }*/
}
