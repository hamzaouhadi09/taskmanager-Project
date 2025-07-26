package com.taskmanager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage {
    private WebDriver driver;

    private By usernameField = By.id("username");
    private By emailField = By.id("email");
    private By passwordField = By.id("password");
    private By registerBtn = By.xpath("//button[contains(text(),'Create an account')]");

    private By emailAlreadyExistsError = By.xpath("//p[contains(text(),'User with this email already exists.')]");
    private By shortPasswordError = By.xpath("//p[contains(text(),'Password must be at least 6 characters.')]");
    private By succesmessage = By.xpath("//p[contains(text(),'User registered successfully.')]");
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void register(String user, String email, String pass) {
        driver.findElement(usernameField).sendKeys(user);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(registerBtn).click();
    }

    public WebElement getEmailExistsError() {
        return driver.findElement(emailAlreadyExistsError);
    }

    public WebElement getShortPasswordError() {
        return driver.findElement(shortPasswordError);
    }
    public WebElement getSuccessMsg() {
        return driver.findElement(succesmessage);
    }
}
