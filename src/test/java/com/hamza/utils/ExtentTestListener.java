package com.hamza.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = ExtentReportManager.createTest(methodName, description);
        test.info("Test execution started: " + methodName);

        // Add test categories based on class name
        String className = result.getTestClass().getName();
        if (className.contains("AuthTests")) {
            test.assignCategory("Authentication");
        } else if (className.contains("TaskTests")) {
            test.assignCategory("Task Management");
        }

        // Add author information
        test.assignAuthor("QA Automation Team");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test completed successfully");
            test.info("Execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, "Test failed with exception: " + result.getThrowable().getMessage());
            test.info("Execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");

        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable().getMessage());
        }
    }

    private String getStackTrace(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.append("\tat ").append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
}