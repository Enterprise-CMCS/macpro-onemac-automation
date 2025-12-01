package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WaiverTypePage {

    private final WebDriver driver;

    private final UIElementUtils ui;

    final String FILE_PATH = "src/test/resources/testDocument.docx";

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

    private static final By WAIVER_1915C_APPENDIX_K_TEMPLATE =
            By.xpath("//label[text()=\"1915(c) Appendix K Amendment Waiver Template\"]/following-sibling::div/input");

    private static final By SUBMIT =
            By.cssSelector("button[data-testid=\"submit-action-form\"]");

    private static final By PACKAGE_SUBMITTED =
            By.xpath("//h3[text()=\"Package submitted\"]");

    public WaiverTypePage(WebDriver driver, UIElementUtils ui) {
        this.driver = driver;
        this.ui = ui;
    }

    public Waiver1915bTypePage openWaiver1915bTypePage() {
        ui.clickElement(WAIVER_1915B_TILE);
        return new Waiver1915bTypePage(driver, ui);
    }


    public void submitWaiver1915cAppendixK(String waiver, String amendmentTitle, String proposedEffectiveDate) {
        ui.clickElement(WAIVER_1915C_APPENDIX_K_TILE);
        ui.sendKeys(AMENDMENT_TITLE, amendmentTitle);
        ui.sendKeys(WAIVER_AMENDMENT_NUMBER, waiver);
        ui.javaScriptClicker(WAIVER_CALENDAR);
        ui.selectFromDropdown(MONTHS, "text", proposedEffectiveDate);
        ui.oneMACCalendarHandler(proposedEffectiveDate);
        ui.uploadFileAndCommit(WAIVER_1915C_APPENDIX_K_TEMPLATE, FILE_PATH);
        ui.clickElement(SUBMIT);
        ui.isVisible(PACKAGE_SUBMITTED);
    }
}
