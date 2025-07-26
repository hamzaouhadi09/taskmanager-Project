package com.hamza.taskmanager;

import com.taskmanager.pages.DashboardPage;
import com.taskmanager.pages.LoginPage;
import com.taskmanager.pages.RegisterPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests extends BaseTest {

    @Test(description = "Verify successful user registration with unique credentials")
    public void testUserRegistration() {
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);

        String username = generateUniqueUsername();
        String email = generateUniqueEmail();
        String password = "password123";

        registerPage.register(username, email, password);

        // Verify redirection to dashboard
        wait.until(ExpectedConditions.urlContains("login"));
        Assert.assertTrue(driver.getCurrentUrl().contains("login"),
                "User should be redirected to dashboard after successful registration");
    }

    @Test(description = "Verify registration success message is displayed")
    public void testRegistrationSuccessMessage() {
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);

        String username = generateUniqueUsername();
        String email = generateUniqueEmail();
        String password = "123456";

        registerPage.register(username, email, password);

        // Wait for and verify success message
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOf(registerPage.getSuccessMsg()));
        Assert.assertTrue(successMsg.isDisplayed(), "Success message should be visible");
        Assert.assertEquals(successMsg.getText(), "User registered successfully.",
                "Success message text should match expected value");
    }

    @Test(description = "Verify registration fails with password less than 6 characters")
    public void testRegistrationWithShortPassword() {
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);

        String username = generateUniqueUsername();
        String email = generateUniqueEmail();
        String shortPassword = "123"; // Less than 6 characters

        registerPage.register(username, email, shortPassword);

        // Verify error message appears
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOf(registerPage.getShortPasswordError()));
        Assert.assertTrue(errorMsg.isDisplayed(), "Short password error should be displayed");
        Assert.assertTrue(errorMsg.getText().contains("Password must be at least 6 characters"),
                "Error message should indicate minimum password length requirement");
    }

    @Test(description = "Verify registration fails with existing email address")
    public void testRegistrationWithExistingEmail() {
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);

        String username = generateUniqueUsername();
        String password = "123456";

        registerPage.register(username, EXISTING_EMAIL, password);

        // Verify duplicate email error
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOf(registerPage.getEmailExistsError()));
        Assert.assertTrue(errorMsg.isDisplayed(), "Email exists error should be displayed");
        Assert.assertTrue(errorMsg.getText().contains("User with this email already exists"),
                "Error message should indicate email already exists");
    }

    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        // Verify redirection to dashboard
        wait.until(ExpectedConditions.urlContains("dashboard"));
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"),
                "User should be redirected to dashboard after successful login");
    }

    @Test(description = "Verify login fails with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        String invalidEmail = "invalid@gmail.com";
        String invalidPassword = "wrongpassword";

        loginPage.login(invalidEmail, invalidPassword);

        // Verify error message is displayed and user stays on login page
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for invalid login");
    }

    @Test(description = "Verify user can successfully logout")
    public void testUserLogout() {
        // Login first
        loginAsValidUser();

        // Perform logout
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.logout();

        // Verify redirection to home page
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        Assert.assertEquals(driver.getCurrentUrl(), BASE_URL + "/",
                "User should be redirected to home page after logout");
    }
}