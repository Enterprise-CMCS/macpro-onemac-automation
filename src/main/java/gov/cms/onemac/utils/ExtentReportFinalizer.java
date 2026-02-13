package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

import java.io.File;

public class ExtentReportFinalizer {

    public static void main(String[] args) {
        try {
            String reportFolder;
            if (args.length > 0) {
                reportFolder = args[0]; // passed as argument
            } else {
                reportFolder = System.getProperty("user.dir") + File.separator + "extent-report";
            }

            File folder = new File(reportFolder);
            if (!folder.exists() || !folder.isDirectory()) {
                throw new RuntimeException("Report folder not found: " + reportFolder);
            }


            String finalHtmlPath = reportFolder + File.separator + "FinalExecutiveReport.html";

            System.out.println("Scanning folder for JSON reports: " + folder.getAbsolutePath());

            ExtentReports extent = new ExtentReports();

            ExtentSparkReporter spark = new ExtentSparkReporter(finalHtmlPath);
            spark.config().setReportName("Consolidated OneMAC Test Report");
            spark.config().setDocumentTitle("OneMAC Test Results");

            extent.attachReporter(spark);


            File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles == null || jsonFiles.length == 0) {
                throw new RuntimeException("No JSON files found in folder: " + folder.getAbsolutePath());
            }

            for (File json : jsonFiles) {
                System.out.println("Merging JSON file: " + json.getName());
                extent.createDomainFromJsonArchive(json);
            }

            extent.flush();

            System.out.println("=== REPORTING COMPLETE ===");
            System.out.println("Final Dashboard: " + finalHtmlPath);

        } catch (Exception e) {
            System.err.println("Failed to finalize reports:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
