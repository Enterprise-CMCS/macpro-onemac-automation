package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SubmissionTypePage {
    private UIElementUtils utils;
    private WebDriver driver;
    private By statePlanAmendment = By.xpath("//h2[text()=\"State Plan Amendment (SPA)\"]/ancestor::a");


    public SubmissionTypePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SpaTypePage selectSpaType() {
        utils.clickElement(statePlanAmendment);
        return new SpaTypePage(driver, utils);
    }


}