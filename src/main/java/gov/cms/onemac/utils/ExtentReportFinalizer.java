package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class ExtentReportFinalizer {

    public static void main(String[] args) {
        // Use "extent-report" as the default base folder
        String baseFolder = (args.length > 0) ? args[0] : "extent-report";
        String mergedJsonPath = baseFolder + "/OneMACTestReport-merged.json";
        String finalHtmlPath = baseFolder + "/FinalExecutiveReport.html";

        try {
            System.out.println("Starting report consolidation from base folder: " + baseFolder);

            File baseDir = new File(baseFolder);
            if (!baseDir.exists() || !baseDir.isDirectory()) {
                System.err.println("Warning: Base folder does not exist. Creating it for future runs.");
                baseDir.mkdirs();
            }

            // Merge JSON files
            mergeAllJsonInFolder(baseDir, mergedJsonPath);

            // Generate consolidated HTML report
            generateHtmlFromJson(mergedJsonPath, finalHtmlPath);

            System.out.println("=== REPORTING COMPLETE ===");
            System.out.println("Merged JSON: " + mergedJsonPath);
            System.out.println("Final HTML Dashboard: " + finalHtmlPath);

        } catch (Exception e) {
            System.err.println("Failed to finalize reports: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Recursively merge all JSON files in a folder and its subfolders
    public static void mergeAllJsonInFolder(File folder, String outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode masterArray = mapper.createArrayNode();

        mergeFolderRecursive(folder, masterArray, mapper);

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), masterArray);
        System.out.println("Merged JSON written to: " + outputPath);
    }

    private static void mergeFolderRecursive(File folder, ArrayNode masterArray, ObjectMapper mapper) throws IOException {
        if (!folder.exists() || !folder.isDirectory()) return;

        // Merge JSON files in current folder
        File[] jsonFiles = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                System.out.println("Merging JSON: " + file.getAbsolutePath());
                ArrayNode node = (ArrayNode) mapper.readTree(file);
                masterArray.addAll(node);
            }
        }

        // Recurse into subfolders
        File[] subDirs = folder.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File sub : subDirs) {
                mergeFolderRecursive(sub, masterArray, mapper);
            }
        }
    }

    // Generate HTML report from merged JSON
    public static void generateHtmlFromJson(String jsonInput, String htmlOutput) throws IOException {
        File sourceJson = new File(jsonInput);
        if (!sourceJson.exists()) {
            System.err.println("No JSON files found to generate HTML. Skipping report generation.");
            return;
        }

        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(htmlOutput);
        spark.config().setReportName("Consolidated Test Execution Report");
        spark.config().setDocumentTitle("OneMAC Final Results");
        extent.attachReporter(spark);

        try {
            extent.createDomainFromJsonArchive(sourceJson);
            extent.flush();
        } catch (Exception e) {
            System.err.println("Error generating HTML from JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
