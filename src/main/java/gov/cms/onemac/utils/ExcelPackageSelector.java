package gov.cms.onemac.utils;

import gov.cms.onemac.models.WaiverPackage;
import gov.cms.onemac.models.SpaPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExcelPackageSelector {

    private static final String FILE = "src/test/resources/packages.xlsx";
    private static final String SHEET = "Packages";
    private static final Random RAND = new Random();

    // ---------------------------------------------------------------
    // Core Excel Reader
    // ---------------------------------------------------------------
    private static List<Row> read() {
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            List<Row> out = new ArrayList<>();
            for (Row r : sh) if (r.getRowNum() > 0) out.add(r);
            return out;
        } catch (Exception e) {
            throw new RuntimeException("Read error", e);
        }
    }

    private static Row pick(List<Row> rows) {
        return rows.isEmpty() ? null : rows.get(RAND.nextInt(rows.size()));
    }

    private static String get(Row r, int col) {
        Cell c = r.getCell(col);
        return c == null ? "" : c.getStringCellValue().trim();
    }

    private static SpaPackage toSpa(Row r) {
        SpaPackage s = new SpaPackage();
        s.setState(get(r, 1));
        s.setAuthority(get(r, 2));
        s.setPackageId(get(r, 4));
        s.setStatus(get(r, 5));
        return s;
    }

    private static WaiverPackage toWaiver(Row r) {
        WaiverPackage w = new WaiverPackage();
        w.setState(get(r,1));
        w.setAuthority(get(r,2));
        w.setActionType(get(r,3));
        w.setPackageId(get(r,4));
        w.setStatus(get(r,5));
        w.setParentId(get(r,6));
        return w;
    }

    private static List<Row> match(String type, String state, String authority, String actionType, String status) {
        List<Row> all = read();
        List<Row> out = new ArrayList<>();
        for (Row r : all) {
            if (type.equalsIgnoreCase(get(r,0)) &&
                    state.equalsIgnoreCase(get(r,1)) &&
                    authority.equalsIgnoreCase(get(r,2)) &&
                    (actionType == null || actionType.equalsIgnoreCase(get(r,3))) &&
                    status.equalsIgnoreCase(get(r,5))) {
                out.add(r);
            }
        }
        return out;
    }

    // ---------------------------------------------------------------
    // SPA SELECTORS
    // ---------------------------------------------------------------
    // Unapproved SPA (Status = "")
    public static SpaPackage selectUnapprovedSPA(String state, String authority) {
        Row r = pick(match("SPA", state, authority, null, "")); // status blank
        if (r == null) throw new RuntimeException("No unapproved SPA found for: " + state + " | " + authority);
        return toSpa(r);
    }

    // Approved SPA (Status = "Approved")
    public static SpaPackage selectApprovedSPA(String state, String authority) {
        Row r = pick(match("SPA", state, authority, null, "Approved"));
        if (r == null) throw new RuntimeException("No approved SPA found for: " + state + " | " + authority);
        return toSpa(r);
    }

    // Approved SPA (Status = "Submitted")
    public static SpaPackage selectSubmittedPA(String state, String authority) {
        Row r = pick(match("SPA", state, authority, null, "Submitted"));
        if (r == null) throw new RuntimeException("No Submitted SPA found for: " + state + " | " + authority);
        return toSpa(r);
    }
    // ---------------------------------------------------------------
    // WAIVER SELECTORS (Final Version)
    // ---------------------------------------------------------------

    // Initial Waivers
    public static WaiverPackage selectUnapprovedInitial(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Initial", ""));
        if (r == null) throw new RuntimeException("No unapproved Initial found: " + state + " | " + authority);
        return toWaiver(r);
    }

    public static WaiverPackage selectApprovedInitial(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Initial", "Approved"));
        if (r == null) throw new RuntimeException("No approved Initial found: " + state + " | " + authority);
        return toWaiver(r);
    }

    // Renewals
    public static WaiverPackage selectUnapprovedRenewal(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Renewal", ""));
        if (r == null) throw new RuntimeException("No unapproved Renewal found: " + state + " | " + authority);
        return toWaiver(r);
    }

    public static WaiverPackage selectApprovedRenewal(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Renewal", "Approved"));
        if (r == null) throw new RuntimeException("No approved Renewal found: " + state + " | " + authority);
        return toWaiver(r);
    }

    // Amendments
    public static WaiverPackage selectUnapprovedAmendment(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Amendment", ""));
        if (r == null) throw new RuntimeException("No unapproved Amendment found: " + state + " | " + authority);
        return toWaiver(r);
    }

    public static WaiverPackage selectApprovedAmendment(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Amendment", "Approved"));
        if (r == null) throw new RuntimeException("No approved Amendment found: " + state + " | " + authority);
        return toWaiver(r);
    }

    // Temporary Extensions
    public static WaiverPackage selectUnapprovedTemporaryExtension(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Temporary Extension", ""));
        if (r == null) throw new RuntimeException("No unapproved TE found: " + state + " | " + authority);
        return toWaiver(r);
    }

    public static WaiverPackage selectApprovedTemporaryExtension(String state, String authority) {
        Row r = pick(match("Waiver", state, authority, "Temporary Extension", "Approved"));
        if (r == null) throw new RuntimeException("No approved TE found: " + state + " | " + authority);
        return toWaiver(r);
    }
}
