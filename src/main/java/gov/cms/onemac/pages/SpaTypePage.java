package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SpaTypePage {

    private WebDriver driver;
    private UIElementUtils utils;
    private By medicaidSPA = By.xpath("//p[text()=\"Submit a new Medicaid State Plan Amendment\"]/ancestor::a");

    public SpaTypePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }


    public MedicaidSpaType selectMedicaidSpa() {
        utils.clickElement(medicaidSPA);
        return new MedicaidSpaType(driver, utils);
    }


}
