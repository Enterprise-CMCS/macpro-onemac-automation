package gov.cms.onemac.pages;

import gov.cms.onemac.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SubmitMedicaidSpaPage {

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
        uiElementUtils.oneMACCalendarHandler(date);
        return this;
    }

  /*  public SubmitMedicaidSpaPage uploadAttachments(String firstFile, String secondFile) throws InterruptedException {
        uiElementUtils.uploadFile(firstFile, CMS179Form);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement cmsUpload = driver.findElement(CMS179Form);
        js.executeScript("arguments[0].dispatchEvent(new Event('change',{bubbles:true}))", cmsUpload);
        js.executeScript("arguments[0].dispatchEvent(new Event('blur',{bubbles:true}))", cmsUpload);
     //   uiElementUtils.isNotVisible(By.cssSelector("svg[data-testid='three-dots-svg']"));
        uiElementUtils.uploadFile(secondFile, spaPages);
        WebElement spaUpload = driver.findElement(spaPages);
        js.executeScript("arguments[0].dispatchEvent(new Event('change',{bubbles:true}))", spaUpload);
        js.executeScript("arguments[0].dispatchEvent(new Event('blur',{bubbles:true}))", spaUpload);
       // uiElementUtils.isNotVisible(By.cssSelector("svg[data-testid='three-dots-svg']"));
        return this;
    }*/

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
}
