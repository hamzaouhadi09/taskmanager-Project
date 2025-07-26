package com.taskmanager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LoginPage {
    private WebDriver driver;

    private By emailField = By.id("email");
    private By passwordField = By.id("password");
    private By loginBtn = By.xpath("//button[contains(text(),'Sign in')]");
    private By errorMsg = By.xpath("//p[text()='Invalid email or password.']");


    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String email, String pass) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(loginBtn).click();
    }

    public boolean isErrorDisplayed() {
        return driver.findElement(errorMsg).isDisplayed();
    }
}