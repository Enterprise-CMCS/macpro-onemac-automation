package gov.cms.onemac.pages;

import gov.cms.onemac.utils.LogHelper;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;


public class StateEarlyAlertPage {
    private static final Logger logger = LogHelper.getLogger();
    private WebDriver driver;
    private UIElementUtils utils;

    //login locators
    private By acceptBTN = By.cssSelector("input[value=\"I Accept\"]");
    private By seaUsername = By.cssSelector("input[id=\"UserName\"]");
    private By seaPassword = By.id("Password");
    private By logOn = By.cssSelector("input[value=\"Log On\"]");

    //create package
    private By stateDropdown = By.id("State_Code");
    private By idNumber = By.id("ID_Number");
    private By authority = By.id("Plan_Type");
    private By initSubDate = By.id("Submission_Date");
    private By confirmSave = By.id("confirm");
    private By statePlanWaiverConfirm = By.xpath("//span[@class=\"ui-button-text\" and text()=\"Confirm\"]");
    private By type = By.id("Service_Type_ID");
    private By typeBtn = By.id("AddServiceType");
    private By serviceSubType = By.id("Service_SubType_ID");
    private By addSubTypeBtn = By.id("AddServiceSubType");

    //Reviewing Entities
    private By leadAnalystID = By.id("Lead_Analyst_ID");

    //Plan Summary Information
    private By subject = By.id("Title_Name");
    private By summaryMemo = By.id("Summary_Memo");
    //Approval/Effective/Expiration Dates
    private By proposedDate = By.id("Proposed_Date");

    //Save
    private By save = By.cssSelector("input[value=\"Save\"]");


    public StateEarlyAlertPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void login(String username, String password) {
        logger.info("Logging in using: {}", username);
        utils.sendKeysByActions(seaUsername, username);
        utils.sendKeysByActions(seaPassword, password);
        utils.clickElement(logOn);
        logger.info("Successfully logged in as: {}", username);

    }

    public void createPackage(String packageID, String initialSubDate, String date) {
        utils.selectFromDropdown(stateDropdown, "value", utils.getStateCode(packageID));
        utils.sendKeys(idNumber, utils.removeStateCode(packageID));
        utils.selectFromDropdown(authority, "text", "Medicaid SPA");
        utils.sendKeys(initSubDate, initialSubDate);
        utils.sendKeys(initSubDate, Keys.TAB);
        utils.clickElement(confirmSave);
        utils.clickElement(statePlanWaiverConfirm);
        utils.selectFromDropdown(type, "text", "Health Homes");
        utils.clickElement(typeBtn);
        utils.selectFromDropdown(serviceSubType, "text", "Regular");
        utils.clickElement(addSubTypeBtn);
        utils.selectFromDropdown(leadAnalystID, "text", "Test2, Test1");
        utils.sendKeys(subject, "Subject Test");
        utils.sendKeys(summaryMemo, "Description test");
        utils.sendKeys(proposedDate, date);
        utils.clickElement(save);
    }
}
