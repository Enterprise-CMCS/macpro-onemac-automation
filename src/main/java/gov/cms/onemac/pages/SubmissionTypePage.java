package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SubmissionTypePage {

    private final WebDriver driver;
    private final UIElementUtils ui;

    private static final By SPA_TITLE =
            By.xpath("//h2[text()='State Plan Amendment (SPA)']/ancestor::a");

    private static final By WAIVER_ACTION_TILE =
            By.xpath("//h2[text()='Waiver Action']/ancestor::a");

    public SubmissionTypePage(WebDriver driver, UIElementUtils ui) {
        this.driver = driver;
        this.ui = ui;
    }

    public SpaPage openSpaTypePage() {
        ui.clickElement(SPA_TITLE);
        return new SpaPage(driver, ui);
    }

    public WaiverTypePage openWaiverTypePage() {
        ui.clickElement(WAIVER_ACTION_TILE);
        return new WaiverTypePage(driver, ui);
    }
}
