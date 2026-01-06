package gov.cms.onemac.pages;

import gov.cms.onemac.utils.ConfigReader;
import gov.cms.onemac.utils.ExcelPackageTracker;
import gov.cms.onemac.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;


public class StateEarlyAlert {

    private static final Logger logger = LogManager.getLogger();
    private WebDriver driver;
    private UIElementUtils utils;

    //login locators
    private By accept = By.id("acceptButton");
    private By seaUsername = By.cssSelector("input[id=\"UserName\"]");
    private By seaPassword = By.id("Password");
    private By logOn = By.cssSelector("input[value=\"Log On\"]");
    //Waivers
    private By actionType = By.id("Action_Type");
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

    //Priority Information
    private By prComments = By.name("Priority_Comments_Memo");
    private By prComplexity = By.id("Priority_Complexity_ID");
    private By prOCDLevel = By.id("Review_Position_ID");

    //Reviewing Entities
    private By leadAnalystID = By.id("Lead_Analyst_ID");

    //Plan Summary Information
    private By subject = By.id("Title_Name");
    private By summaryMemo = By.id("Summary_Memo");
    //Approval/Effective/Expiration Dates
    private By proposedDate = By.id("Proposed_Date");
    private By approvedEffecDate = By.id("Approved_Effective_Date");
    //Request for Additional Information locators

    private By addRAI = By.id("addRAI");
    private By RAIRequestDate = By.id("RAIs_0__RAI_Requested_Date");
    private By secondRAIRequestDate = By.id("RAIs_1__RAI_Requested_Date");
    //Save
    private By save = By.cssSelector("input[value=\"Save\"]");

    private By successMessage = By.cssSelector("p[class=\"returnMessage\"]");

    //Manage SPA/Waivers/Demonstrations

    private By searchId = By.id("Search_ID_Number");

    private By spaButton = By.id("btnSubmit");

    private By statePlanGrid = By.xpath("//div[@id=\"statePlanGrid\"]//tbody/tr");

    private By editStateAction = By.xpath("//a[contains(@title,\"Edit State Action\")]");

    private By raiReceivedDate = By.id("RAIs_0__RAI_Received_Date");

    private By responseWithdrawnDate = By.id("RAIs_0__RAI_Withdrawn_Date");

    private By addNew = By.cssSelector("a[title=\"Add new SPA or Waiver\"]");

    private By seaAddEdit = By.xpath("//a[text()=\"SEA Add/Edit\"]");

    private By completionStatus = By.id("SPW_Status_ID");

    //OCD/Group Director/ARA Review
    private By ocdGroupDirector = By.id("OCD_Review_ID");


    public StateEarlyAlert(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void login() {
        logger.info("Navigating to SEATool...");
        String username = ConfigReader.getUsername("sea");
        driver.get(utils.getSeaToolEnv());
        utils.clickElement(accept);
        logger.info("Logging as: " + username);
        utils.sendKeysByActions(seaUsername, username);
        utils.sendKeysByActions(seaPassword, ConfigReader.getPassword("sea"));
        utils.clickElement(logOn);
        logger.info("Successfully logged in as: " + username);
    }

    public void navigateToSEATool() {
        logger.info("Navigating to SEATool...");
        driver.get(utils.getSeaToolEnv());
    }

    public void addRaiResponseReceivedDate(String spaId, String raiResponseDate) {
        logger.info("Adding Response Date in SEATool for: {}", spaId);
        utils.clickElement(seaAddEdit);
        utils.sendKeys(searchId, spaId);
        utils.clickElement(spaButton);
        utils.waitForNumberOfElementsToBe(statePlanGrid, 1);
        utils.clickElement(editStateAction);
        utils.sendKeys(raiReceivedDate, raiResponseDate);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully Added RAI Response Date as {} for SPA: {}.", raiResponseDate, spaId);
    }

    public void addRaiResponseWithdrawnDate(String spaId, String raiWithdrawalDate) {
        utils.clickElement(seaAddEdit);
        utils.sendKeys(searchId, spaId);
        utils.clickElement(spaButton);
        utils.waitForNumberOfElementsToBe(statePlanGrid, 1);
        utils.clickElement(editStateAction);
        utils.sendKeys(responseWithdrawnDate, raiWithdrawalDate);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
    }

    public void createSPAPackage(String packageID, String initialSubDate, String proposedEffectiveDate) {
        login();
        logger.info("Creating SPA package: {} in SEATool...", packageID);
        utils.clickElement(seaAddEdit);
        utils.clickElement(addNew);
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
        utils.sendKeys(proposedDate, proposedEffectiveDate);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully created SPA Package: {} in SEATool.", packageID);
        ExcelPackageTracker.updateStatus(packageID, "Under Review");
    }

    public void createWaiverPackage(String packageID, String initialSubDate, String proposedEffectiveDate) {
        login();
        logger.info("Creating waiver package: {} in SEATool...", packageID);
        utils.clickElement(seaAddEdit);
        utils.clickElement(addNew);
        utils.selectFromDropdown(stateDropdown, "value", utils.getStateCode(packageID));
        utils.sendKeys(idNumber, utils.removeStateCode(packageID));
        utils.selectFromDropdown(authority, "text", "1915(c)");
        utils.sendKeys(initSubDate, initialSubDate);
        utils.sendKeys(initSubDate, Keys.TAB);
        utils.clickElement(confirmSave);
        utils.clickElement(statePlanWaiverConfirm);
        utils.selectFromDropdown(actionType, "text", "Amend");
        utils.selectFromDropdown(type, "text", "1915(c) Waivers Do Not Use");
        utils.clickElement(typeBtn);
        utils.selectFromDropdown(serviceSubType, "text", "Other Do Not Use");
        utils.clickElement(addSubTypeBtn);
        utils.selectFromDropdown(leadAnalystID, "text", "Test2, Test1");
        utils.sendKeys(subject, "Subject Test");
        utils.sendKeys(summaryMemo, "Description test");
        utils.sendKeys(proposedDate, proposedEffectiveDate);
     /*   utils.clickElement(addRAI);
        utils.sendKeys(RAIRequestDate, utils.getRaiRequestDate());*/
        utils.clickElement(save);
        utils.isVisible(successMessage);
        logger.info("Successfully created waiver package: {} in SEATool.", packageID);
        utils.safelyAcceptAlert();
    }

    public void createAppendixKWaiverPackage(String packageID, String initialSubDate, String proposedEffectiveDate) {
        login();
        logger.info("Creating waiver package: {} in SEATool...", packageID);
        utils.clickElement(seaAddEdit);
        utils.clickElement(addNew);
        utils.selectFromDropdown(stateDropdown, "value", utils.getStateCode(packageID));
        utils.sendKeys(idNumber, utils.removeStateCode(packageID));
        utils.selectFromDropdown(authority, "text", "1915(c)");
        utils.sendKeys(initSubDate, initialSubDate);
        utils.sendKeys(initSubDate, Keys.TAB);
        utils.clickElement(confirmSave);
        utils.clickElement(statePlanWaiverConfirm);
        utils.selectFromDropdown(actionType, "text", "Amend");
        utils.selectFromDropdown(type, "text", "1915(c) Waivers Do Not Use");
        utils.clickElement(typeBtn);
        utils.selectFromDropdown(serviceSubType, "text", "Other Do Not Use");
        utils.clickElement(addSubTypeBtn);
        utils.selectFromDropdown(leadAnalystID, "text", "Test2, Test1");
        utils.sendKeys(subject, "Subject Test");
        utils.sendKeys(summaryMemo, "Description test");
        utils.sendKeys(proposedDate, proposedEffectiveDate);
        utils.clickElement(addRAI);
        utils.sendKeys(RAIRequestDate, utils.getRaiRequestDate());
        utils.clickElement(save);
        utils.isVisible(successMessage);
        logger.info("Successfully created waiver package: {} in SEATool.", packageID);
        utils.safelyAcceptAlert();
    }

    public void requestRAI(String packageID, String initialSubDate, String proposedEffDate, String raiRequestDate, String spaAuthority, String svcType, String svcSubType) {
        login();
        logger.info("Creating SPA in SEATool and Requesting RAI...");
        utils.clickElement(seaAddEdit);
        utils.clickElement(addNew);
        utils.selectFromDropdown(stateDropdown, "value", utils.getStateCode(packageID));
        utils.sendKeys(idNumber, utils.removeStateCode(packageID));
        utils.selectFromDropdown(authority, "text", spaAuthority);
        utils.sendKeys(initSubDate, initialSubDate);
        utils.sendKeys(initSubDate, Keys.TAB);
        utils.clickElement(confirmSave);
        utils.clickElement(statePlanWaiverConfirm);
        if (spaAuthority.contains("CHIP")) {
            utils.sendKeys(prComments, "Test PR Comments Memo");
            utils.selectFromDropdown(prComplexity, "text", "1 - Low Complexity");
            utils.selectFromDropdown(prOCDLevel, "text", "P1a - Center Director Signs");
        }
        utils.selectFromDropdown(type, "text", svcType);
        utils.clickElement(typeBtn);
        utils.selectFromDropdown(serviceSubType, "text", svcSubType);
        utils.clickElement(addSubTypeBtn);
        utils.selectFromDropdown(leadAnalystID, "text", "Test2, Test1");
        utils.sendKeys(subject, "Subject Test");
        utils.sendKeys(summaryMemo, "Description test");
        utils.sendKeys(proposedDate, proposedEffDate);
        utils.clickElement(addRAI);
        utils.sendKeys(RAIRequestDate, raiRequestDate);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully created SPA: {} and requested RAI in SEATool.", packageID);
    }

    public void requestRaiForChipSpa(String spaId, String raiRequestDate) {
        logger.info("Adding second RAI Date in SEATool for: {}", spaId);
        utils.clickElement(seaAddEdit);
        utils.sendKeys(searchId, spaId);
        utils.clickElement(spaButton);
        utils.waitForNumberOfElementsToBe(statePlanGrid, 1);
        utils.clickElement(editStateAction);
        utils.clickElement(addRAI);
        utils.sendKeys(secondRAIRequestDate, raiRequestDate);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully Added second RAI Date as {} for SPA: {}.", raiRequestDate, spaId);
    }

    public void updatePackageStatus(String packageID, String status) {
        login();
        logger.info("Updating package status in SEATool to {}", status);
        utils.clickElement(seaAddEdit);
        utils.sendKeys(searchId, packageID);
        utils.clickElement(spaButton);
        utils.waitForNumberOfElementsToBe(statePlanGrid, 1);
        utils.clickElement(editStateAction);
        utils.sendKeys(approvedEffecDate, utils.getInitialSubmissionDate());
        utils.selectFromDropdown(completionStatus, "text", status);
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully updated package status to: {}", status);
        ExcelPackageTracker.updateStatus(packageID, status);

    }

    public void markPackageApproved(String packageID) {
        logger.info("Marking package {} as Approved in SEATool.", packageID);
        utils.clickElement(seaAddEdit);
        utils.sendKeys(searchId, packageID);
        utils.clickElement(spaButton);
        utils.waitForNumberOfElementsToBe(statePlanGrid, 1);
        utils.clickElement(editStateAction);
        utils.selectFromDropdown(ocdGroupDirector, "text", "Yes");
        utils.clickElement(save);
        utils.isVisible(successMessage);
        utils.safelyAcceptAlert();
        logger.info("Successfully marked package {} as Approved in SEATool.", packageID);
        ExcelPackageTracker.updateStatus(packageID, "Approved");
    }


}

