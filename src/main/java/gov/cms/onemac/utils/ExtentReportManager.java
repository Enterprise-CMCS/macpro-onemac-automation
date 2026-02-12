package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports();

            // 1. Setup HTML Report (Spark)
            ExtentSparkReporter spark = new ExtentSparkReporter("extent-report/OneMACTestReport.html");
            spark.config().setReportName("OneMAC Automated Test Report");
            spark.config().setDocumentTitle("Test Results");

            // 2. Setup JSON Report
            // In 5.1.1, the class is JsonFormatter
            JsonFormatter json = new JsonFormatter("extent-report/OneMACTestReport.json");

            // 3. Attach both reporters
            extent.attachReporter(spark, json);
        }
        return extent;
    }
}
