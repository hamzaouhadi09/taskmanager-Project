package com.hamza.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest test;
    private static String reportPath;

    public static void initReports() {
        if (extent == null) {
            // Create reports directory if it doesn't exist
            File reportsDir = new File("test-output/extent-reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            // Generate timestamp for unique report names
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            reportPath = "test-output/extent-reports/TaskManager-Test-Report-" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Task Manager E2E Test Report");
            sparkReporter.config().setReportName("Task Manager Automation Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System information
            extent.setSystemInfo("Application", "Task Manager");
            extent.setSystemInfo("Environment", "QA Test Environment");
            extent.setSystemInfo("Base URL", "https://cs-qa-test-commonshare-14f55a8d.koyeb.app");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }
    }

    public static ExtentTest createTest(String testName, String description) {
        test = extent.createTest(testName, description);
        return test;
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReports generated: " + reportPath);
        }
    }

    public static void markTestResult(ITestResult result) {
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    test.pass("Test passed successfully");
                    break;
                case ITestResult.FAILURE:
                    test.fail("Test failed: " + result.getThrowable().getMessage());
                    break;
                case ITestResult.SKIP:
                    test.skip("Test skipped: " + result.getThrowable().getMessage());
                    break;
            }
        }
    }

    public static void addScreenshot(String screenshotPath) {
        if (test != null && screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
        }
    }
}