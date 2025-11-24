package gov.cms.onemac.tests;

import gov.cms.onemac.base.BaseTest;
import gov.cms.onemac.flows.CMSUser;
import gov.cms.onemac.flows.SeaUser;
import gov.cms.onemac.flows.StateUser;
import gov.cms.onemac.models.SpaPackage;
import gov.cms.onemac.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;



public class E2E extends BaseTest {

    private static final Logger logger = LogManager.getLogger();
    private CMSUser cms;
    private StateUser state;
    private SpaPackage spa;
    private SeaUser seaUser;

    @Test
    public void initialSubmissionWorkflow() {
        spa = SpaGenerator.createSpa("MD", "Medicaid SPA");

        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);

        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );

        state.navigateToOneMac();
        state.openPackage(spa);
        boolean stateUnderReviewVisible = state.isPackageStatusUnderReview();
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean cmsPendingStatusVisible = cms.isPackageStatusPending();
        AssertionUtil.assertTrue(
                stateUnderReviewVisible,
                "State user should see the package status as 'Under Review'."
        );

        AssertionUtil.assertTrue(
                cmsPendingStatusVisible,
                "CMS user should see the package status as 'Pending'."
        );
    }


    @Test
    public void verifyRaiIssuedWorkflow() {
        spa = SpaGenerator.createSpa("MD", "Medicaid SPA");
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();

        restartDriver();

        CMSUser cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isStatusPendingRai = cms.isStatusPendingRAI();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see the package status reflecting that an RAI has been issued."
        );

        AssertionUtil.assertTrue(
                isStatusPendingRai,
                "CMS user should see the package status as 'Pending RAI'."
        );
    }


    @Test
    public void verifyRaiResponseFlow() {
        spa = SpaGenerator.createSpa("MD", "Medicaid SPA");
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        state.respondToRAI();
        boolean isPackageStatusSubmitted = state.isPackageStatusSubmitted();
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        AssertionUtil.assertTrue(
                isRaiIssuedVisible,
                "State user should see that an RAI has been issued."
        );

        AssertionUtil.assertTrue(
                isFormalRaiLinkVisible,
                "State user should see the 'Respond to Formal RAI' link."
        );

        AssertionUtil.assertTrue(
                isPackageStatusSubmitted,
                "Package status should be updated to 'Submitted' after responding to RAI."
        );

        AssertionUtil.assertTrue(
                isIntakeNeededVisible,
                "CMS user should see the package status as 'Submitted - Intake Needed'."
        );
    }


    @Test
    public void verifyEnableDisableRaiResponseWithdrawalRefactored() {
        spa = SpaGenerator.createSpa("MD", "Medicaid SPA");
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.requestRai(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate(),
                getUtils().getRaiRequestDate()
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isRaiIssuedVisible = state.isRaiIssued();
        boolean isFormalRaiLinkVisible = state.isFormalRaiLinkVisible();
        state.respondToRAI();
        boolean isPackageStatusSubmitted = state.isPackageStatusSubmitted();
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeededVisible = cms.isStatusSubmittedIntakeNeeded();
        seaUser = createNewSeaUser();
        seaUser.login();
        //Navigate back to SEATool to add RAI response date
        seaUser.addResponseReceivedDate(spa, getUtils().getRaiResponseDate());
        //Navigate back to OneMAC as CMS user session at this step keeps cms user logged in (no need to sign in again)
        cms.navigateToOneMac();
        cms.openPackage(spa);
        boolean isEnableFormalRAIResponseLinkVisible = cms.isEnableFormalRAIResponseLinkVisible();
        cms.enableRAIResponseWithdraw();
        restartDriver();
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.openPackage(spa);
        boolean isWithdrawFormalRAIResponseLinkVisible = state.isWithdrawFormalRAIResponseLinkVisible();
        state.withdrawFormalRaiResponse("Testing RAI Response withdrawal");
        //Check status updates to: Formal RAI Response - Withdrawal Requested for state user
        boolean isStatusUpdatedToFormalRAIResponseWithdrawalRequestedForStateUser = state.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        //Check status updates to: Formal RAI Response - Withdrawal Requested for CMS user
        restartDriver();
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isStatusUpdatedToFormalRAIResponseWithdrawalRequestedForCMSUser = cms.isStatusUpdatedToFormalRAIResponseWithdrawalRequested();
        //Navigate back to SeaTool to add RAI response withdrawal date
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.addRaiResponseWithdrawnDate(spa, getUtils().getRaiResponseWithdrawnDate());
    }


    @Test
    public void dataGeneratorTest() {
        // PackagePoolGenerator.generate(List.of("MD","CO","AL","NY"));
//Generate a SPA package
// SpaPackage spaPackage = SpaGenerator.createSpa("MD", "Medicaid SPA");


//  System.out.println(spaPackage.getPackageId());


//Generate Renewal based on approved initial
   /*    WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("CO", "1915(b)");

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
        spa = SpaGenerator.createSpa("MD", "Medicaid SPA");
        state = createNewStateUser();
        state.navigateToOneMac();
        state.login();
        state.submitPackage(spa, getUtils().getProposedEffectiveDate());
        state.verifySpaSubmitted(spa);
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.createPackage(
                spa,
                getUtils().getInitialSubmissionDate(),
                getUtils().getProposedEffectiveDate()
        );
        state.navigateToOneMac();
        state.openPackage(spa);
        boolean isUnderReview = state.isPackageStatusUnderReview();
        boolean canUploadSubsequentDocs = state.isUploadSubsequentDocumentsLinkPresent();
        boolean canWithdraw = state.isWithdrawPackageLinkPresent();
        state.withdrawPackage(spa, "Package withdrawal test");
        boolean isWithdrawalShown = state.isWithdrawalStatusPresent();
        restartDriver();
        CMSUser cms = createNewCMSUser();
        cms.login();
        cms.openPackage(spa);
        boolean isIntakeNeeded = cms.isPackageStatusSubmittedIntakeNeeded();
        restartDriver();
        seaUser = createNewSeaUser();
        seaUser.login();
        seaUser.updatePackageStatus(spa, "Withdrawn");
        cms = createNewCMSUser();
        cms.navigateToOneMac();
        cms.login();
        cms.openPackage(spa);
        boolean isCMSWithdrawn = cms.isPackageStatusWithdrawn();
        restartDriver();
        state = createNewStateUser();
        state.login();
        state.openPackage(spa);
        boolean isStateWithdrawn = state.isPackageStatusWithdrawn();
        AssertionUtil.assertTrue(isUnderReview, "Package should be under review.");
        AssertionUtil.assertTrue(canUploadSubsequentDocs, "Upload Subsequent Documents link should be shown.");
        AssertionUtil.assertTrue(canWithdraw, "Withdraw Package link should be shown.");
        AssertionUtil.assertTrue(isWithdrawalShown, "Withdrawal status should appear for State user.");
        AssertionUtil.assertTrue(isIntakeNeeded, "CMS should see 'Submitted - Intake Needed'.");
        AssertionUtil.assertTrue(isCMSWithdrawn, "CMS should see the package as Withdrawn.");
        AssertionUtil.assertTrue(isStateWithdrawn, "State should see the package as Withdrawn.");
    }

}










