package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Waiver1915bTypePage {

        private WebDriver driver;
        private UIElementUtils utils;
        private By waiver1915b4FFSLink  = By.xpath("//h2[text()=\"1915(b)(4) FFS Selective Contracting Waivers\"]/ancestor::a");

        public Waiver1915bTypePage(WebDriver driver, UIElementUtils utils) {
            this.driver = driver;
            this.utils = utils;
        }

        public Waiver1915b4AuthorityPage selectWaiver1915b4Authority(){
            utils.clickElement(waiver1915b4FFSLink);
            return new Waiver1915b4AuthorityPage(driver,utils);
        }
    }
