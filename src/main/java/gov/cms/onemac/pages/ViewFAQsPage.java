package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ViewFAQsPage {

    private WebDriver driver;
    private UIElementUtils ui;

    private static final By VIEW_FAQs_TAB = By.cssSelector("a[data-testid=\"View FAQs-d\"]");

    public ViewFAQsPage(WebDriver driver, UIElementUtils ui) {
        this.driver = driver;
        this.ui = ui;
    }

    public boolean isViewFAQsTabVisible(){
        return ui.isVisible(VIEW_FAQs_TAB);
    }
}
