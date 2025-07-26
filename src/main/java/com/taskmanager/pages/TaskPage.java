package com.taskmanager.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class TaskPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public TaskPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //CREATE TASK ELEMENTS
    private By titleField = By.id("title");
    private By descField = By.id("description");
    private By dueDateField = By.id("dueDate");
    private By createPrioritySelect = By.id("priority");
    private By saveBtn = By.cssSelector("button[type='submit']");
    private By taskSuccessMsg = By.xpath("//p[text()='Task created successfully.']");

    //TASK LIST
    private By firstTaskTitle = By.cssSelector("h3.text-lg.font-semibold");

    private By completeCheckbox = By.cssSelector(".task-item:first-child input[type='checkbox']");

    //DETAILS
    private By detailTitle = By.xpath("//h3[contains(@class, 'text-lg')]");
    private By detailDescription = By.xpath("//p[contains(@class, 'text-sm')]");
    private By detailPriority = By.xpath("//p[strong[text()='Priority:']]//span");
    private By detailDueDate = By.xpath("//p[strong[text()='Due:']]");
    private By detailStatus = By.xpath("//p[strong[text()='Status:']]");

    //DELETE
    private By firstDeleteButton  =  By.xpath("(//button[contains(text(),'Delete')])[1]");

    //EDIT
    private By editButton = By.xpath("(//button[contains(text(), 'Edit')])[1]");
    private By editTitleInput = By.id("edit-title");
    private By editDescTextarea = By.id("edit-description");
    private By editDueDateInput = By.id("edit-dueDate");
    private By editPrioritySelect = By.id("edit-priority");
    private By editCheckbox = By.id("edit-isComplete");
    private By saveChangesButton = By.xpath("//button[contains(text(), 'Save Changes')]");
    private By toggleStatusButton = By.xpath("//button[contains(text(),'Mark Complete') or contains(text(),'Mark Incomplete')]");
    private By statusText = By.xpath("//p[strong[contains(text(),'Status')]]");

    //METHODS

    // ✅ CREATE
    public void createTask(String title, String description, String dueDate, String priority) {
        driver.findElement(titleField).sendKeys(title);
        driver.findElement(descField).sendKeys(description);
        driver.findElement(dueDateField).sendKeys(dueDate);

        Select dropdown = new Select(driver.findElement(createPrioritySelect));
        dropdown.selectByVisibleText(priority);

        driver.findElement(saveBtn).click();
    }

    public void waitForSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(taskSuccessMsg));
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(taskSuccessMsg)).getText();
    }

    // 🔍 VIEW
    public String getFirstTaskTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstTaskTitle)).getText();
    }

    public boolean isTaskPresent(String expectedTitle) {
        List<WebElement> titles = driver.findElements(By.xpath("//h3[contains(@class, 'text-lg')]"));
        return titles.stream().anyMatch(el -> el.getText().trim().equals(expectedTitle.trim()));
    }

    private By taskTitleInList(String title) {
        return By.xpath("//h3[contains(text(),'" + title + "')]");
    }

    public void clickOnTaskTitle(String title) {
        wait.until(ExpectedConditions.elementToBeClickable(taskTitleInList(title))).click();
    }

    public String getDetailTitle() {
        return driver.findElement(detailTitle).getText();
    }

    public String getDetailDescription() {
        return driver.findElement(detailDescription).getText();
    }

    public String getDetailPriority() {
        return driver.findElement(detailPriority).getText();
    }

    public String getDetailDueDate() {
        return driver.findElement(detailDueDate).getText();
    }

    public String getDetailStatus() {
        return driver.findElement(detailStatus).getText();
    }

    // ✏️ EDIT (via modal)
    public void clickEditButton() {
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }

    public void setEditTitle(String title) {
        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(editTitleInput));
        titleField.clear();
        titleField.sendKeys(title);
    }

    public void setEditDescription(String desc) {
        WebElement descField = wait.until(ExpectedConditions.visibilityOfElementLocated(editDescTextarea));
        descField.clear();
        descField.sendKeys(desc);
    }

    public void setEditDueDate(String date) {
        WebElement dateField = wait.until(ExpectedConditions.visibilityOfElementLocated(editDueDateInput));
        dateField.clear();
        dateField.sendKeys(date);
    }

    public void selectEditPriority(String priority) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(editPrioritySelect));
        new Select(dropdown).selectByVisibleText(priority);
    }

    public void setIsComplete(boolean complete) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(editCheckbox));
        if (checkbox.isSelected() != complete) {
            checkbox.click();
        }
    }

    public void clickSaveChanges() {
        wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton)).click();
    }

    // COMPLETE
    public void toggleFirstTaskCompletion() {
        driver.findElement(completeCheckbox).click();
    }

    // DELETE
    public void deleteFirstTask() {

            wait.until(ExpectedConditions.elementToBeClickable(firstDeleteButton)).click();

            try {
                driver.switchTo().alert().accept();
            } catch (NoAlertPresentException ignored){}

    }

    // Utility

    public String getStatusText() {
        return driver.findElement(statusText).getText();
    }

    public String getToggleButtonText() {
        return driver.findElement(toggleStatusButton).getText();
    }

    public String getToggleButtonClass() {
        return driver.findElement(toggleStatusButton).getAttribute("class");
    }

    public void toggleTaskStatus() {
        driver.findElement(toggleStatusButton).click();
    }
    public void submitTaskFormWithEmptyTitle(String description) {
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.clear();

        WebElement descriptionInput = driver.findElement(By.id("description"));
        descriptionInput.clear();
        descriptionInput.sendKeys(description);

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
    }

    public int getNumberOfTasks() {
        return driver.findElements(By.cssSelector(".task-card")).size();
    }


}
