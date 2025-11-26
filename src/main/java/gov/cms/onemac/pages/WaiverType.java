package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WaiverType {

    private WebDriver driver;
    private UIElementUtils utils;
    private By waiver1915bActionsLink = By.xpath("//h2[text()=\"1915(b) Waiver Actions\"]/ancestor::a");

    public WaiverType(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public Waiver1915bTypePage selectWaiver1915bType() {
        utils.clickElement(waiver1915bActionsLink);
        return new Waiver1915bTypePage(driver, utils);
    }

}
