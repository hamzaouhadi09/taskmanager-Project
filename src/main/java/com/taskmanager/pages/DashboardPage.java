package com.taskmanager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {
    private WebDriver driver;
    By logoutBtn = By.cssSelector("button.w-full.text-left");

    private By navToggleBtn = By.xpath("//button[@data-collapse-toggle='navbar-default']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void logout() {
        openMenu();
        driver.findElement(logoutBtn).click();
    }
    public void openMenu() {
        driver.findElement(navToggleBtn).click();
    }
}