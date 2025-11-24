package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MedicaidSpaType {

    private By allOtherMedicaidSpa = By.xpath("//h2[text()=\"All Other Medicaid SPA Submissions\"]/ancestor::a");
    private WebDriver driver;
    private UIElementUtils utils;



    public MedicaidSpaType(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SubmitMedicaidSpaPage allOtherMedicaidSpa() {
        utils.clickElement(allOtherMedicaidSpa);
        return new SubmitMedicaidSpaPage(driver, utils);
    }
}
