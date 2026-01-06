package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.SeaUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.AssertionUtil;
import gov.cms.onemac.utils.ExcelPackageTracker;
import org.testng.annotations.Test;


public class PackageLifecycleE2ETests extends BaseTest {


    @Test
    public void verifyInitialMedicaidSpaSubmissionWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");

        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending'."
        );
    }

    @Test
    public void verifyInitialChipSpaSubmissionWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentCHIPSPA("MD", "CHIP SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Affordable Care Act", "MAGI-CHIP"
        );
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending'."
        );
    }

    @Test
    public void verifyRaiIssuedWorkflowCHIPSPA() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentCHIPSPA("MD", "CHIP SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Affordable Care Act", "MAGI-CHIP"
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see the package status indicating that an RAI has been issued."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingRaiVisible = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(
                isPendingRaiVisible,
                "CMS user should see the package status as 'Pending RAI'."
        );

    }


    @Test
    public void verifyRAIChipSpaRequestResponseWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentCHIPSPA("MD", "CHIP SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Affordable Care Act", "MAGI-CHIP"

        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see that an RAI has been issued."
        );
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        AssertionUtil.assertTrue(
                isFormalRaiLinkVisible,
                "State user should see the 'Respond to Formal RAI' link."
        );
        state.respondToRAI(spa.getAuthority());
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();
        AssertionUtil.assertTrue(
                isSubmittedAfterResponse,
                "Package status should be updated to 'Submitted' after responding to RAI."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS user should see the package status as 'Submitted - Intake Needed'."
        );
        // ---------- SEA: Add Response Received Date ----------
        sea = createNewSeaUser();
        sea.login();
        sea.addResponseReceivedDate(spa, getUtils().getRaiResponseDate());
        // ---------- CMS: Enable Formal RAI Response Withdrawal ----------
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isEnableFormalRaiResponseLinkVisible = cms.isEnableFormalRAIResponseLinkVisible();
        AssertionUtil.assertTrue(isEnableFormalRaiResponseLinkVisible, "Enable Formal RAI link should be visible CMS users.");
        //Need code to add another RAI for this CHIP SPA
        sea.navigateToSEATool();
        sea.requestRaiForChipSPA(spa.getPackageId(), getUtils().getSecondRaiRequestDate());
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isRaiIssued = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssued,
                "State user should see the package status indicating that an RAI has been issued."
        );
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingRaiVisible = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(
                isPendingRaiVisible,
                "CMS user should see the package status as 'Pending RAI'."
        );
    }

    @Test
    public void verifyRaiResponseWithdrawalEnableDisable() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Health Homes", "Regular"
        );
        // ---------- State: RAI Issued + Respond to Formal RAI ----------
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        AssertionUtil.assertTrue(isRaiIssuedVisible, "RAI Issued link should be visible for state users.");
        AssertionUtil.assertTrue(isFormalRaiLinkVisible, "Formal RAI link should be visible for state users.");
        state.respondToRAI(spa.getAuthority());
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();
        AssertionUtil.assertTrue(isSubmittedAfterResponse, "Submitted status should be visible for state users.");
        // ---------- CMS: sees Submitted - Intake Needed ----------
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(isIntakeNeededVisible, "Intake Needed should be visible for CMS users.");
        // ---------- SEA: Add Response Received Date ----------
        sea = createNewSeaUser();
        sea.login();
        sea.addResponseReceivedDate(spa, getUtils().getRaiResponseDate());
        // ---------- CMS: Enable Formal RAI Response Withdrawal ----------
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isEnableFormalRaiResponseLinkVisible = cms.isEnableFormalRAIResponseLinkVisible();
        AssertionUtil.assertTrue(isEnableFormalRaiResponseLinkVisible, "Enable Formal RAI link should be visible CMS users.");
        cms.enableRAIResponseWithdraw();
        // ---------- State: Withdraw Formal RAI Response ----------
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isWithdrawFormalRaiResponseLinkVisible = state.isWithdrawFormalRAIResponseLinkVisible();
        AssertionUtil.assertTrue(isWithdrawFormalRaiResponseLinkVisible, "Withdraw formal RAI link should be visible State users.");
        state.withdrawFormalRaiResponse("Testing RAI Response withdrawal");
        boolean isStateStatusUpdatedToRaiResponseWithdrawalRequested =
                state.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        AssertionUtil.assertTrue(isStateStatusUpdatedToRaiResponseWithdrawalRequested, "State users should see status 'Formal RAI Response withdrawal Requested'.");
        // ---------- CMS: Validate RAI Response Withdrawal Requested ----------
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isCMSStatusUpdatedToRaiResponseWithdrawalRequested =
                cms.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        AssertionUtil.assertTrue(isCMSStatusUpdatedToRaiResponseWithdrawalRequested, "CMS users should see status 'Formal RAI Response withdrawal Requested'.");
        // ---------- SEA: Add RAI Response Withdrawal Date ----------
        sea = createNewSeaUser();
        sea.login();
        sea.addRaiResponseWithdrawnDate(spa, getUtils().getRaiResponseWithdrawnDate());
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isPackageStatusPendingRAI = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(isPackageStatusPendingRAI, "CMS users should see status 'Pending RAI'.");
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isRaiIssued = state.isRaiIssued();
        AssertionUtil.assertTrue(isRaiIssued, "State users should see status 'RAI Issued'.");
    }

    @Test
    public void verify1915cAppendixKWaiverAmendmentWorkflow() {
        // ---------- Arrange ----------
        StateUser state = createNewStateUser();
        String amendment = state.submitWaiver1915cAppendixK("Test Amendment", getUtils().getProposedEffectiveDate());

        SeaUser sea = createNewSeaUser();
        sea.createWaiverPackage(
                amendment, getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        state.navigateToOneMac();
        state.openWaiverPackage(amendment);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see that an RAI has been issued."
        );
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        AssertionUtil.assertTrue(
                isFormalRaiLinkVisible,
                "State user should see the 'Respond to Formal RAI' link."
        );
        state.respondToAppendixKWaiverRAI();
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();
        AssertionUtil.assertTrue(
                isSubmittedAfterResponse,
                "Package status should be updated to 'Submitted' after responding to RAI."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(amendment);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS user should see the package status as 'Submitted - Intake Needed'."
        );

    }

    @Test
    public void verifyRaiIssuedMedicaidSpaWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Health Homes", "Regular"
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see the package status indicating that an RAI has been issued."
        );
        ExcelPackageTracker.updateStatus(spa.getPackageId(), "RAI Issued");
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingRaiVisible = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(
                isPendingRaiVisible,
                "CMS user should see the package status as 'Pending RAI'."
        );

    }


    @Test
    public void verifyRaiResponseMedicaidSpaWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate(), spa.getAuthority(), "Health Homes", "Regular"
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see that an RAI has been issued."
        );
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        AssertionUtil.assertTrue(
                isFormalRaiLinkVisible,
                "State user should see the 'Respond to Formal RAI' link."
        );
        state.respondToRAI(spa.getAuthority());
        boolean isSubmittedAfterResponse = state.isPackageStatusSubmitted();
        AssertionUtil.assertTrue(
                isSubmittedAfterResponse,
                "Package status should be updated to 'Submitted' after responding to RAI."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS user should see the package status as 'Submitted - Intake Needed'."
        );
    }


    @Test
    public void verifyWithdrawalRequestedPackageWithdrawnMedicaidSpaWorkflow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
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
        state.withdrawPackage(spa, "Package withdrawal test");
        boolean isWithdrawalStatusVisible = state.isWithdrawalStatusPresent();
        AssertionUtil.assertTrue(
                isWithdrawalStatusVisible,
                "Withdrawal status banner should appear after the State user withdraws the package."
        );
        // ---------- CMS: sees Submitted - Intake Needed ----------
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isPackageStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS should see the status as 'Submitted - Intake Needed' after State withdrawal."
        );
        // ---------- SEA: update to Withdrawn ----------
        restartDriver();
        sea = createNewSeaUser();
        sea.updatePackageStatus(spa, "Withdrawn");
        // ---------- CMS: verify Withdrawn ----------
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isCMSWithdrawnVisible = cms.isPackageStatusWithdrawn();
        AssertionUtil.assertTrue(
                isCMSWithdrawnVisible,
                "CMS should see the package as 'Withdrawn' after SEA updates the status."
        );
        // ---------- State: verify Withdrawn ----------
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isStateWithdrawnVisible = state.isPackageStatusWithdrawn();
        AssertionUtil.assertTrue(
                isStateWithdrawnVisible,
                "State should see the package as 'Withdrawn' after SEA updates the status."
        );
    }


    @Test
    public void verifyUploadSubsequentDocuments() {
        // ---------- Arrange ----------
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
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

        state.uploadSubsequentDocuments("This is an automated test script");
        boolean isCoverLetterVisible = state.isCoverLetterPresent(); // <-- FIXED
        AssertionUtil.assertTrue(
                isCoverLetterVisible,
                "Cover Letter document should be present after uploading subsequent documents."
        );
    }


    @Test
    public void verifyApprovalFlow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the package status as 'Under Review' before approval."
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the package status as 'Pending' prior to approval."
        );

        sea = createNewSeaUser();
        sea.login();
        sea.markPackageApproved(spa);
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isApprovedVisible = state.isPackageStatusApproved();
        AssertionUtil.assertTrue(
                isApprovedVisible,
                "State user should see the package status as 'Approved' after SEA approval."
        );
    }


    @Test
    public void verifyPackageDisapprovalFlow() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");

        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
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
        StateUser state = createNewStateUser();
        String amendment = state.createFFSSelectiveContractingInitialWaiver();
        SeaUser sea = createNewSeaUser();
        sea.createWaiverPackage(
                amendment,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        // ---------- Act: State checks initial status ----------
        state.navigateToOneMac();
        state.openWaiverPackage(amendment);
        boolean isUnderReviewVisible = state.isPackageStatusUnderReview();
        AssertionUtil.assertTrue(
                isUnderReviewVisible,
                "State user should see the waiver status as 'Under Review'."
        );
        // ---------- CMS sees Pending ----------
        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(amendment);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see the waiver status as 'Pending'."
        );
        // ---------- SEA updates to Terminated ----------
        sea = createNewSeaUser();
        sea.updatePackageStatus(amendment, "Terminated");
        // ---------- State validates final status ----------
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openWaiverPackage(amendment);
        boolean isTerminatedVisible = state.isPackageStatusTerminated();
        AssertionUtil.assertTrue(
                isTerminatedVisible,
                "State user should see the waiver status as 'Terminated'."
        );
    }


    @Test
    public void verifyPendingConcurrence() {
        StateUser state = createNewStateUser();
        SpaPackage spa = state.submitNewStateAmendmentSPA("MD", "Medicaid SPA");
        SeaUser sea = createNewSeaUser();
        sea.createSPAPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );

        // ---------- SEA updates status to Pending-Concurrence ----------
        sea = createNewSeaUser();
        sea.updatePackageStatus(spa, "Pending-Concurrence");
        // ---------- CMS verifies updated status ----------
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isPendingConcurrenceVisible = cms.isPackageStatusPendingConcurrence();
        AssertionUtil.assertTrue(
                isPendingConcurrenceVisible,
                "CMS user should see the package status as 'Pending-Concurrence'."
        );
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


    }

    @Test
    public void verifyPendingApprovalWorkflow() {
        StateUser state = createNewStateUser();
        String amendment = state.submitWaiver1915cAppendixK("Test Amendment", getUtils().getProposedEffectiveDate());
        SeaUser sea = createNewSeaUser();
        sea.createWaiverPackage(
                amendment, getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openWaiverPackage(amendment);
        boolean isPendingVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );
        // SEA updates package status to Pending-Approval
        sea = createNewSeaUser();
        sea.updatePackageStatus(amendment, "Pending-Approval");
        // Re-open package as CMS to verify status
        cms.navigateToOneMac();
        cms.openWaiverPackage(amendment);
        boolean isPendingApprovalVisible = cms.isPackageStatusPendingApproval();
        AssertionUtil.assertTrue(
                isPendingApprovalVisible,
                "CMS user should see the package status as 'Pending-Approval'."
        );
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
    }

    @Test
    public void verifyUnsubmittedWorkflow() {
        // ---------- Arrange ----------
        StateUser state = createNewStateUser();
        String amendment = state.submitWaiver1915cAppendixK("Test Amendment", getUtils().getProposedEffectiveDate());

        SeaUser sea = createNewSeaUser();
        sea.createWaiverPackage(
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
        AssertionUtil.assertTrue(
                isPendingVisible,
                "CMS user should see package status as 'Pending'."
        );
        // SEA updates package status to Unsubmitted
        sea = createNewSeaUser();
        sea.updatePackageStatus(amendment, "Unsubmitted");
        // Re-open package as CMS to verify status
        cms.navigateToOneMac();
        cms.openWaiverPackage(amendment);
        boolean isCMSUnsubmitted = cms.isUnsubmittedVisible();
        AssertionUtil.assertTrue(
                isCMSUnsubmitted,
                "CMS user should see the package status as 'Unsubmitted'."
        );
        // ---------- State checks after SEA and CMS updates ----------
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openWaiverPackage(amendment);
        boolean isStateUnsubmitted = state.isPackageStatusUnsubmitted();
        boolean isSubsequentDocumentsLinkNotVisible = state.isUploadSubsequentDocumentsLinkNotVisible();
        AssertionUtil.assertTrue(
                isSubsequentDocumentsLinkNotVisible,
                "State user should NOT see the 'Upload Subsequent Documents' link."
        );
        AssertionUtil.assertTrue(
                isStateUnsubmitted,
                "State user should see the package status as 'Unsubmitted'."
        );
    }

    @Test
    public void dataGeneratorTest() {
        //   PackagePoolGenerator.generate(List.of("MD","CO","AL","NY"));
        //Generate  SPA package
//paPackage spaPackage = SpaGenerator.createSpa("MD", "Medicaid SPA");

        // String waiver = WaiverIdGenerator.nextInitial("MD");

//  System.out.println(spaPackage.getPackageId());


//Generate Renewal based on approved initial
   /*  WaiverPackage parent = ExcelPackageSelector.selectWaiver("MD", "1915(b)","Initial","Approved");

        List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );
 /*
//Generate Renewal based on approved renewal
        WaiverPackage parent = ExcelPackageSelector.selectUnapprovedRenewal("MD", "1915(b)");

        List<String> existing = ExcelPackageTracker.readAllIds(parent.getState());

        String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(), existing);

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Renewal",
                renewalId, "", parent.getPackageId()
        );

//Generate Temporary Extension based on approved initial
        WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );

//Generate Temporary Extension based on approved renewal
         WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String te = ExcelPackageTracker.nextTemporaryExtensionId(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Temporary Extension",
                te, "", parent.getPackageId()
        );

//Generate amendment based on an approved initial
        WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );

//Generate amendment based on an approved renewal
       WaiverPackage parent = ExcelPackageSelector.selectApprovedRenewal("MD", "1915(b)");

        String amendment = ExcelPackageTracker.nextAmendmentFromParent(parent.getPackageId());

        ExcelPackageTracker.appendNewPackage(
                "Waiver", parent.getState(), parent.getAuthority(), "Amendment",
                amendment, "", parent.getPackageId()
        );
    }*/

    }
}












