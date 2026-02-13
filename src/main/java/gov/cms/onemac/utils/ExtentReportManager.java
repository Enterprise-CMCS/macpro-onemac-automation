package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

import java.io.File;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {

        if (extent == null) {

            extent = new ExtentReports();

            // Base report directory (portable across OS)
            String baseDir = System.getProperty("user.dir")
                    + File.separator + "extent-report";

            new File(baseDir).mkdirs();

            // Determine unique run identifier
            String runId = System.getProperty("run.id");

            if (runId == null || runId.isEmpty()) {
                runId = System.getenv("GITHUB_RUN_ID");
            }

            if (runId == null || runId.isEmpty()) {
                runId = "local-" + System.currentTimeMillis();
            }

            // Build dynamic file names
            String htmlPath = baseDir + File.separator +
                    "OneMAC-TestReport-" + runId + ".html";

            String jsonPath = baseDir + File.separator +
                    "OneMACTestReport-" + runId + ".json";

            // Spark Reporter (HTML)
            ExtentSparkReporter spark = new ExtentSparkReporter(htmlPath);
            spark.config().setReportName("OneMAC SMART Automated Test Report");
            spark.config().setDocumentTitle("Test Results");

            // JSON Reporter (Required for merging)
            JsonFormatter json = new JsonFormatter(jsonPath);

            extent.attachReporter(spark, json);

            System.out.println("Extent HTML Report: " + htmlPath);
            System.out.println("Extent JSON Report: " + jsonPath);
        }

        return extent;
    }
}
