package gov.cms.onemac.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtentReportFinalizer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ExtentReportFinalizer <parent-reports-folder>");
            System.exit(1);
        }

        String parentFolder = args[0];
        String mergedJsonPath = parentFolder + File.separator + "OneMACTestReport-merged.json";
        String finalHtmlPath = parentFolder + File.separator + "FinalExecutiveReport.html";

        try {
            System.out.println("Starting report consolidation...");

            // 1. Scan parent folder recursively and collect all JSON files
            List<File> jsonFiles = findAllJsonFiles(new File(parentFolder));

            if (jsonFiles.isEmpty()) {
                System.err.println("No JSON report files found under " + parentFolder);
                System.exit(2);
            }

            // 2. Merge all JSON files
            mergeJsonFiles(jsonFiles, mergedJsonPath);

            // 3. Generate HTML report from merged JSON
            generateHtmlFromJson(mergedJsonPath, finalHtmlPath);

            System.out.println("=== REPORTING COMPLETE ===");
            System.out.println("Final Dashboard: " + finalHtmlPath);

        } catch (Exception e) {
            System.err.println("Failed to finalize reports: " + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }
    }

    /** Recursively finds all .json files in a folder */
    private static List<File> findAllJsonFiles(File folder) {
        List<File> jsonFiles = new ArrayList<>();
        if (!folder.exists() || !folder.isDirectory()) return jsonFiles;

        File[] files = folder.listFiles();
        if (files == null) return jsonFiles;

        for (File file : files) {
            if (file.isDirectory()) {
                jsonFiles.addAll(findAllJsonFiles(file));
            } else if (file.getName().endsWith(".json")) {
                jsonFiles.add(file);
            }
        }
        return jsonFiles;
    }

    /** Merge multiple JSON files into a single ArrayNode and write to output */
    private static void mergeJsonFiles(List<File> jsonFiles, String outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode masterArray = mapper.createArrayNode();

        for (File file : jsonFiles) {
            System.out.println("Merging: " + file.getAbsolutePath());
            ArrayNode individualArray = (ArrayNode) mapper.readTree(file);
            masterArray.addAll(individualArray);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), masterArray);
    }

    /** Generate HTML report from merged JSON using ExtentSparkReporter */
    private static void generateHtmlFromJson(String jsonInput, String htmlOutput) throws IOException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(htmlOutput);

        spark.config().setReportName("Consolidated OneMAC Test Report");
        spark.config().setDocumentTitle("OneMAC Final Results");

        extent.attachReporter(spark);

        File sourceJson = new File(jsonInput);
        if (sourceJson.exists()) {
            extent.createDomainFromJsonArchive(sourceJson);
            extent.flush();
        } else {
            throw new IOException("Merged JSON source not found at: " + jsonInput);
        }
    }
}
