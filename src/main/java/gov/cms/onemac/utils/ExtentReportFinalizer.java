package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class ExtentReportFinalizer {

    public static void main(String[] args) {
        // Accept base folder path from args or default to "reports"
        String baseFolder = (args.length > 0) ? args[0] : "reports";
        String mergedJsonPath = baseFolder + "/OneMACTestReport-merged.json";
        String finalHtmlPath = baseFolder + "/FinalExecutiveReport.html";

        try {
            System.out.println("Starting report consolidation...");

            // Merge JSON files from all subfolders
            mergeAllJsonInFolder(baseFolder, mergedJsonPath);

            // Generate HTML from merged JSON
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

    /**
     * Recursively scans a folder for all JSON files and merges them into one ArrayNode
     */
    public static void mergeAllJsonInFolder(String folderPath, String outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode masterArray = mapper.createArrayNode();

        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Base folder not found: " + folderPath);
        }

        // Recursively scan subfolders
        File[] jsonFiles = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                System.out.println("Merging JSON: " + file.getAbsolutePath());
                ArrayNode individualFileNode = (ArrayNode) mapper.readTree(file);
                masterArray.addAll(individualFileNode);
            }
        }

        // Scan subfolders
        File[] subdirs = dir.listFiles(File::isDirectory);
        if (subdirs != null) {
            for (File sub : subdirs) {
                mergeSubfolderJson(sub, masterArray, mapper);
            }
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), masterArray);
    }

    private static void mergeSubfolderJson(File folder, ArrayNode masterArray, ObjectMapper mapper) throws IOException {
        File[] files = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                System.out.println("Merging JSON: " + file.getAbsolutePath());
                ArrayNode node = (ArrayNode) mapper.readTree(file);
                masterArray.addAll(node);
            }
        }

        // Recursive for nested folders
        File[] subdirs = folder.listFiles(File::isDirectory);
        if (subdirs != null) {
            for (File sub : subdirs) {
                mergeSubfolderJson(sub, masterArray, mapper);
            }
        }
    }

    /**
     * Generates the Extent Spark HTML report from merged JSON
     */
    public static void generateHtmlFromJson(String jsonInput, String htmlOutput) throws IOException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(htmlOutput);
        spark.config().setReportName("Consolidated Test Execution Report");
        spark.config().setDocumentTitle("OneMAC Final Results");

        extent.attachReporter(spark);

        File sourceJson = new File(jsonInput);
        if (sourceJson.exists()) {
            extent.createDomainFromJsonArchive(sourceJson);
            extent.flush();
        } else {
            throw new IOException("Merged JSON source not found: " + jsonInput);
        }
    }
}
