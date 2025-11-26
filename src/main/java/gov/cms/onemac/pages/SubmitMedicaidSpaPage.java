package gov.cms.onemac.pages;

import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.ExcelPackageTracker;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SubmitMedicaidSpaPage {

    private static final Logger logger = LogManager.getLogger();
    final String filePath = "src/test/resources/testDocument.docx";

    private WebDriver driver;

    private UIElementUtils uiElementUtils;

    private By spaIDField = By.id("spa-id");

    private By medicaidSpaCalendar = By.cssSelector("button[data-testid=\"proposedEffectiveDate-datepicker\"]");

    private By CMS179Form = By.xpath("//label[text()=\"CMS-179 Form\"]/following-sibling::div/input");

    private By spaPages = By.xpath("//label[text()=\"SPA Pages\"]/following-sibling::div/input");

    private By saveSpa = By.cssSelector("button[data-testid=\"submit-action-form\"]");

    private By packageSubmitted = By.xpath("//h3[text()=\"Package submitted\"]");

    private By addInfo = By.tagName("textarea");

    private By months = By.name("months");


    public SubmitMedicaidSpaPage(WebDriver driver, UIElementUtils uiElementUtils) {
        this.driver = driver;
        this.uiElementUtils = uiElementUtils;
    }


    public SubmitMedicaidSpaPage enterSpaId(String spaId) {
        uiElementUtils.sendKeys(spaIDField, spaId);
        return this;
    }

    public SubmitMedicaidSpaPage pickEffectiveDate(String date) {
        uiElementUtils.javaScriptClicker(medicaidSpaCalendar);
        uiElementUtils.selectFromDropdown(months, "text", date);
        uiElementUtils.oneMACCalendarHandler(date);
        return this;
    }


    public void submit() {
        uiElementUtils.saveSpa(saveSpa);
    }

    public boolean isSubmitted() {
        return uiElementUtils.isVisible(packageSubmitted);
    }

    public SubmitMedicaidSpaPage uploadAttachments() {
        uiElementUtils.uploadFileAndCommit(CMS179Form, filePath);
        uiElementUtils.uploadFileAndCommit(spaPages, filePath);
        return this;
    }

    public SubmitMedicaidSpaPage isSpaSubmitted(SpaPackage spaPackage) {
        if (isSubmitted()) {
            logger.info("Successfully submitted Spa: {} in OneMAC.", spaPackage.getPackageId());
            ExcelPackageTracker.updateStatus(spaPackage.getPackageId(), "Submitted");
        }
        return this;
    }

}
