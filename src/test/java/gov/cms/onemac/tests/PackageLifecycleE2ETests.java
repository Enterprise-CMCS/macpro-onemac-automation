package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.SeaUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.models.WaiverPackage;
import gov.cms.onemac.utils.*;
import org.testng.annotations.Test;


public class PackageLifecycleE2ETests extends BaseTest {


    @Test
    public void initialSubmissionWorkflow() {

        // ---------- Arrange ----------
       // SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");
        StateUser state = createNewStateUser();
        state.submitMedicaidSPA("MD","Medicaid SPA");
       /* state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);*/

     /*   SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- Act ----------
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();

        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending'."
        );*/
    }


    @Test
    public void verifyRaiIssuedWorkflow() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );

        // ---------- Act ----------
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();

        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingRaiVisible = cms.isStatusPendingRAI();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see the package status indicating that an RAI has been issued."
        );

        AssertionUtil.assertTrue(
                isPendingRaiVisible,
                "CMS user should see the package status as 'Pending RAI'."
        );
    }


    @Test
    public void verifyRaiResponseFlow() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );

        // ---------- Act ----------
        state.navigateToOneMac();
        state.openPackage(spa);

        boolean isRaiIssuedVisible = state.isRaiIssued();
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();

        state.respondToRAI();
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();

        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);

        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see that an RAI has been issued."
        );

        AssertionUtil.assertTrue(
                isFormalRaiLinkVisible,
                "State user should see the 'Respond to Formal RAI' link."
        );

        AssertionUtil.assertTrue(
                isSubmittedAfterResponse,
                "Package status should be updated to 'Submitted' after responding to RAI."
        );

        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS user should see the package status as 'Submitted - Intake Needed'."
        );
    }


    @Test
    public void verifyEnableDisableRaiResponseWithdrawal() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );

        // ---------- State: RAI Issued + Respond to Formal RAI ----------
        state.navigateToOneMac();
        state.openPackage(spa);

        boolean isRaiIssuedVisible = state.isRaiIssued();
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        AssertionUtil.assertTrue(isRaiIssuedVisible,"RAI Issued link should be visible for state users.");
        AssertionUtil.assertTrue(isFormalRaiLinkVisible,"Formal RAI link should be visible for state users.");
        state.respondToRAI();
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();
        AssertionUtil.assertTrue(isSubmittedAfterResponse,"Submitted status should be visible for state users.");
        // ---------- CMS: sees Submitted - Intake Needed ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);

        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(isIntakeNeededVisible,"Intake Needed should be visible for CMS users.");

        // ---------- SEA: Add Response Received Date ----------
        sea = createNewSeaUser();
        sea.login();
        sea.addResponseReceivedDate(spa, getUtils().getRaiResponseDate());

        // ---------- CMS: Enable Formal RAI Response Withdrawal ----------
        cms.navigateToOneMac();
        cms.openPackage(spa);

        boolean isEnableFormalRaiResponseLinkVisible = cms.isEnableFormalRAIResponseLinkVisible();
        AssertionUtil.assertTrue(isEnableFormalRaiResponseLinkVisible,"Enable Formal RAI link should be visible CMS users.");
        cms.enableRAIResponseWithdraw();

        // ---------- State: Withdraw Formal RAI Response ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);

        boolean isWithdrawFormalRaiResponseLinkVisible = state.isWithdrawFormalRAIResponseLinkVisible();
        AssertionUtil.assertTrue(isWithdrawFormalRaiResponseLinkVisible,"Withdraw formal RAI link should be visible State users.");
        state.withdrawFormalRaiResponse("Testing RAI Response withdrawal");

        boolean isStateStatusUpdatedToRaiResponseWithdrawalRequested =
                state.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        AssertionUtil.assertTrue(isStateStatusUpdatedToRaiResponseWithdrawalRequested,"State users should see status 'Formal RAI Response withdrawal Requested'.");

        // ---------- CMS: Validate RAI Response Withdrawal Requested ----------
        restartDriver();

        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);

        boolean isCMSStatusUpdatedToRaiResponseWithdrawalRequested =
                cms.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        AssertionUtil.assertTrue(isCMSStatusUpdatedToRaiResponseWithdrawalRequested,"CMS users should see status 'Formal RAI Response withdrawal Requested'.");
        // ---------- SEA: Add RAI Response Withdrawal Date ----------
        sea = createNewSeaUser();
        sea.login();
        sea.addRaiResponseWithdrawnDate(spa, getUtils().getRaiResponseWithdrawnDate());

        cms.navigateToOneMac();
        cms.openPackage(spa);

        boolean isPackageStatusPendingRAI = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(isPackageStatusPendingRAI,"CMS users should see status 'Pending RAI'.");
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);

        boolean isRaiIssued = state.isRaiIssued();
        AssertionUtil.assertTrue(isRaiIssued,"State users should see status 'Pending RAI'.");
    }


    @Test
    public void dataGeneratorTest() {
        //   PackagePoolGenerator.generate(List.of("MD","CO","AL","NY"));
//Generate  SPA package
// SpaPackage spaPackage = SpaGenerator.createSpa("MD", "Medicaid SPA");

        //  String waiver = WaiverIdGenerator.nextInitial("MD");

//  System.out.println(spaPackage.getPackageId());


//Generate Renewal based on approved initial
  /*     WaiverPackage parent = ExcelPackageSelector.selectWaiver("CO", "1915(b)","");

        List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );*/

//Generate Renewal based on approved renewal
        //*WaiverPackage parent = ExcelPackageSelector.selectUnapprovedRenewal("MD", "1915(b)");

      /*  List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );
*/
//Generate Temporary Extension based on approved initial
     /*   WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());


        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );*/

//Generate Temporary Extension based on approved renewal
  /*       WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );*/

//Generate amendment based on an approved initial
     /*   WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );*/

//Generate amendment based on an approved renewal
  /*     WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );*/
    }


    @Test
    public void verifyWithdrawalRequestedPackageWithdrawnFlow() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- State: initial package actions ----------
        state.navigateToOneMac();
        state.openPackage(spa);

        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        boolean isSubsequentDocsLinkVisible = state.isUploadSubsequentDocumentsLinkPresent();
        boolean isWithdrawLinkVisible = state.isWithdrawPackageLinkPresent();

        state.withdrawPackage(spa, "Package withdrawal test");
        boolean isWithdrawalStatusVisible = state.isWithdrawalStatusPresent();

        // ---------- CMS: sees Submitted - Intake Needed ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isPackageStatusSubmittedIntakeNeeded();

        // ---------- SEA: update to Withdrawn ----------
        restartDriver();

        sea = createNewSeaUser();
        sea.login();
        sea.updatePackageStatus(spa, "Withdrawn");

        // ---------- CMS: verify Withdrawn ----------
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isCMSWithdrawnVisible = cms.isPackageStatusWithdrawn();

        // ---------- State: verify Withdrawn ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isStateWithdrawnVisible = state.isPackageStatusWithdrawn();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "Package should initially be in 'Under Review' status."
        );

        AssertionUtil.assertTrue(
                isSubsequentDocsLinkVisible,
                "'Upload Subsequent Documents' link should be visible prior to withdrawal."
        );

        AssertionUtil.assertTrue(
                isWithdrawLinkVisible,
                "'Withdraw Package' link should be visible to the State user."
        );

        AssertionUtil.assertTrue(
                isWithdrawalStatusVisible,
                "Withdrawal status banner should appear after the State user withdraws the package."
        );

        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS should see the status as 'Submitted - Intake Needed' after State withdrawal."
        );

        AssertionUtil.assertTrue(
                isCMSWithdrawnVisible,
                "CMS should see the package as 'Withdrawn' after SEA updates the status."
        );

        AssertionUtil.assertTrue(
                isStateWithdrawnVisible,
                "State should see the package as 'Withdrawn' after SEA updates the status."
        );
    }


    @Test
    public void verifyUploadSubsequentDocuments() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- Act ----------
        state.navigateToOneMac();
        state.openPackage(spa);

        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        boolean isSubsequentDocsLinkVisible = state.isUploadSubsequentDocumentsLinkPresent();
        boolean isWithdrawLinkVisible = state.isWithdrawPackageLinkPresent();

        state.uploadSubsequentDocuments("This is an automated test script");

        boolean isCoverLetterVisible = state.isCoverLetterPresent(); // <-- FIXED

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                isSubsequentDocsLinkVisible,
                "'Upload Subsequent Documents' link should be visible to the state user."
        );

        AssertionUtil.assertTrue(
                isWithdrawLinkVisible,
                "'Withdraw Package' link should be present before uploading additional documents."
        );

        AssertionUtil.assertTrue(
                isCoverLetterVisible,
                "Cover Letter document should be present after uploading subsequent documents."
        );
    }


    @Test
    public void verifyApprovalFlow() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- State Initial Validation ----------
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();

        // ---------- CMS Pending Validation ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // ---------- SEA Approves Package ----------
        sea = createNewSeaUser();
        sea.login();
        sea.markPackageApproved(spa);

        // ---------- State Final Validation ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isApprovedVisible = state.isPackageStatusApproved();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review' before approval."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending' prior to approval."
        );

        AssertionUtil.assertTrue(
                isApprovedVisible,
                "State user should see the package status as 'Approved' after SEA approval."
        );
    }


    @Test
    public void verifyPackageDisapprovalFlow() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- State User: Check initial status ----------
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();

        // ---------- CMS: Verify Pending status ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // ---------- SEA: Update package to Disapproved ----------
        sea = createNewSeaUser();
        sea.login();
        sea.updatePackageStatus(spa, "Disapproved");

        // ---------- State User: Validate Disapproved status ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isDisapprovedVisible = state.isPackageStatusDisapproved();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review' before SEA changes it."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending' prior to SEA disapproval."
        );

        AssertionUtil.assertTrue(
                isDisapprovedVisible,
                "State user should see the package status as 'Disapproved' after SEA updates it."
        );
    }


    @Test
    public void verifyWaiverTerminationFlow() {

        // ---------- Arrange ----------
        String waiverId = WaiverIdGenerator.nextInitial("MD");

        // Track the waiver in Excel for E2E traceability
        ExcelPackageTracker.appendNewPackage(
                "Waiver", "MD", "1915(b)", "Initial", waiverId, "", ""
        );

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitWaiver(waiverId);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createWaiver(
                waiverId,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- Act: State checks initial status ----------
        state.navigateToOneMac();
        state.openWaiverPackage(waiverId);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();

        // ---------- CMS sees Pending ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(waiverId);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // ---------- SEA updates to Terminated ----------
        sea = createNewSeaUser();
        sea.login();
        sea.updatePackageStatus(waiverId, "Terminated");

        // ---------- State validates final status ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openWaiverPackage(waiverId);
        boolean isTerminatedVisible = state.isPackageStatusTerminated();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the waiver status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the waiver status as 'Pending'."
        );

        AssertionUtil.assertTrue(
                isTerminatedVisible,
                "State user should see the waiver status as 'Terminated'."
        );
    }


    @Test
    public void verifyPendingConcurrence() {

        // ---------- Arrange ----------
        SpaPackage spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- CMS views initial status ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // ---------- SEA updates status to Pending-Concurrence ----------
        sea = createNewSeaUser();
        sea.login();
        sea.updateStatus(spa, "Pending-Concurrence");

        // ---------- CMS verifies updated status ----------
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isPendingConcurrenceVisible = cms.isPackageStatusPendingConcurrence();

        // ---------- State validates UI behavior ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);

        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        boolean isSubsequentDocsLinkHidden = state.isUploadSubsequentDocumentsLinkNotVisible();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                isSubsequentDocsLinkHidden,
                "State user should NOT see the 'Upload Subsequent Documents' link."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );

        AssertionUtil.assertTrue(
                isPendingConcurrenceVisible,
                "CMS user should see the package status as 'Pending-Concurrence'."
        );
    }

    @Test
    public void verifyPendingApprovalWorkflow() {

        // ---------- Arrange ----------
        WaiverPackage parent = ExcelPackageSelector.selectWaiver("MD", "1915(c)", "Initial", "Approved");
        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());
        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitWaiver1915c(amendment, "Test Amendment", getUtils().getProposedEffectiveDate());


        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createWaiver(
                amendment, getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- Act ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(amendment);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // SEA updates package status to Pending-Approval
        sea = createNewSeaUser();
        sea.login();
        sea.updatePackageStatus(amendment, "Pending-Approval");

        // Re-open package as CMS to verify status
        cms.navigateToOneMac();
        cms.openWaiverPackage(amendment);
        boolean isPendingApprovalVisible = cms.isPackageStatusPendingApproval();

        // ---------- State checks after SEA and CMS updates ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openWaiverPackage(amendment);

        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        boolean isSubsequentDocumentsLinkNotVisible = state.isUploadSubsequentDocumentsLinkNotVisible();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                isSubsequentDocumentsLinkNotVisible,
                "State user should NOT see the 'Upload Subsequent Documents' link."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );

        AssertionUtil.assertTrue(
                isPendingApprovalVisible,
                "CMS user should see the package status as 'Pending-Approval'."
        );
    }


    @Test
    public void verifyUnsubmittedWorkflow() {

        // ---------- Arrange ----------
        WaiverPackage parent = ExcelPackageSelector.selectWaiver("MD", "1915(c)", "Initial", "Approved");
        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());
        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );

        StateUser state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitWaiver1915c(amendment, "Test Amendment", getUtils().getProposedEffectiveDate());


        SeaUser sea = createNewSeaUser();
        sea.login();
        sea.createWaiver(
                amendment, getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        // ---------- Act ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(amendment);
        boolean isPendingVisible = cms.isPackageStatusPending();

        // SEA updates package status to Unsubmitted
        sea = createNewSeaUser();
        sea.login();
        sea.updatePackageStatus(amendment, "Unsubmitted");

        // Re-open package as CMS to verify status
        cms.navigateToOneMac();
        cms.openWaiverPackage(amendment);
        boolean isCMSUnsubmitted = cms.isUnsubmittedVisible();

        // ---------- State checks after SEA and CMS updates ----------
        restartDriver();

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openWaiverPackage(amendment);

        boolean isStateUnsubmitted = state.isPackageStatusUnsubmitted();
        boolean isSubsequentDocumentsLinkNotVisible = state.isUploadSubsequentDocumentsLinkNotVisible();

        // ---------- Assert ----------
        AssertionUtil.assertTrue(
                isStateUnsubmitted,
                "State user should see the package status as 'Unsubmitted'."
        );

        AssertionUtil.assertTrue(
                isSubsequentDocumentsLinkNotVisible,
                "State user should NOT see the 'Upload Subsequent Documents' link."
        );

        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );

        AssertionUtil.assertTrue(
                isCMSUnsubmitted,
                "CMS user should see the package status as 'Unsubmitted'."
        );
    }


}












