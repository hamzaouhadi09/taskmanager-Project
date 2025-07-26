package com.hamza.taskmanager;

import com.aventstack.extentreports.ExtentTest;
import com.taskmanager.pages.LoginPage;
import com.hamza.utils.ExtentReportManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ExtentTest test;

    // Configuration constants
    protected static final String BASE_URL = "https://cs-qa-test-commonshare-14f55a8d.koyeb.app";
    protected static final String LOGIN_URL = BASE_URL + "/auth/login";
    protected static final String REGISTER_URL = BASE_URL + "/auth/register";
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

    // Test user credentials
    protected static final String VALID_EMAIL = "valid@gmail.com";
    protected static final String VALID_PASSWORD = "valid06";
    protected static final String EXISTING_EMAIL = "searschris289@gmail.com";

    @BeforeSuite
    public void setUpSuite() {
        ExtentReportManager.initReports();
    }

    @BeforeMethod
    public void setUp(Method method) {
        // Initialize ExtentTest for each test method
        Test testAnnotation = method.getAnnotation(Test.class);
        String testName = method.getName();
        String testDescription = testAnnotation != null ? testAnnotation.description() : "No description provided";

        test = ExtentReportManager.createTest(testName, testDescription);
        test.info("Setting up test environment for: " + testName);

        WebDriverManager.chromedriver().setup();

        // Configure Chrome options for better stability
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-debugging-port=0");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().deleteAllCookies();

        test.info("Navigating to base URL: " + BASE_URL);
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String screenshotPath = null;

        if (result.getStatus() == ITestResult.FAILURE) {
            screenshotPath = takeScreenshot(result.getName());
            test.fail("Test failed. Screenshot captured.");
            if (screenshotPath != null) {
                ExtentReportManager.addScreenshot("../screenshots/" + new File(screenshotPath).getName());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test completed successfully");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test was skipped");
        }

        ExtentReportManager.markTestResult(result);

        if (driver != null) {
            driver.quit();
            test.info("Browser closed successfully");
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentReportManager.flushReports();
    }

    private String takeScreenshot(String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            String fileName = testName + "_" + System.currentTimeMillis() + ".png";
            String filePath = "screenshots/" + fileName;
            Files.copy(screenshot.toPath(), Paths.get(filePath));
            System.out.println("Screenshot saved: " + fileName);
            return filePath;
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    // Utility methods for common operations
    protected void navigateToLogin() {
        test.info("Navigating to login page");
        driver.get(LOGIN_URL);
    }

    protected void navigateToRegister() {
        test.info("Navigating to register page");
        driver.get(REGISTER_URL);
    }

    protected String generateUniqueEmail() {
        String email = "user" + System.currentTimeMillis() + "@gmail.com";
        test.info("Generated unique email: " + email);
        return email;
    }

    protected String generateUniqueUsername() {
        String username = "user" + System.currentTimeMillis();
        test.info("Generated unique username: " + username);
        return username;
    }

    protected void loginAsValidUser() {
        test.info("Logging in as valid user");
        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);
        wait.until(ExpectedConditions.urlContains("dashboard"));
        test.pass("Successfully logged in and navigated to dashboard");
    }
}