package gov.cms.onemac.pages;

import gov.cms.onemac.utils.ExcelPackageTracker;
import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Waiver1915b4AuthorityPage {

    final String filePath = "src/test/resources/testDocument.docx";
    private WebDriver driver;
    private UIElementUtils utils;
    private By submit = By.cssSelector("button[data-testid=\"submit-action-form\"]");
    private By packageSubmitted = By.xpath("//h3[text()=\"Package submitted\"]");
    private By initialWaiver = By.xpath("//h2[text()=\"1915(b)(4) FFS Selective Contracting New Initial Waiver\"]/ancestor::a");
    private By waiverId = By.id("id");
    private By months = By.name("months");
    private By waiverCalendar = By.cssSelector("button[data-testid=\"proposedEffectiveDate-datepicker\"]");
    private By waiverApplication = By.xpath("//label[text()=\"1915(b)(4) FFS Selective Contracting (Streamlined) Waiver Application Pre-print\"]/following-sibling::div/input");

    public Waiver1915b4AuthorityPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public Waiver1915b4AuthorityPage selectInitialWaiver() {
        utils.clickElement(initialWaiver);
        return this;
    }

    public Waiver1915b4AuthorityPage enterWaiverId(String spaId) {
        utils.sendKeys(waiverId, spaId);
        return this;
    }

    public Waiver1915b4AuthorityPage pickEffectiveDate(String date) {
        utils.javaScriptClicker(waiverCalendar);
        utils.selectFromDropdown(months, "text", date);
        utils.oneMACCalendarHandler(date);
        return this;
    }

    public Waiver1915b4AuthorityPage uploadAttachment() {
        utils.uploadFileAndCommit(waiverApplication, filePath);
        return this;
    }

    public Waiver1915b4AuthorityPage submitPackage() {
        utils.clickElement(submit);
        return this;
    }

    public void isSpaSubmitted(String waiver) {
        if (isSubmitted()) {
            // logger.info("Successfully submitted Waiver: {} in OneMAC.", spaPackage.getPackageId());
            ExcelPackageTracker.updateStatus(waiver, "Submitted");
        }
    }

    public boolean isSubmitted() {
        return utils.isVisible(packageSubmitted);
    }
}
