package com.hamza.taskmanager;

import com.taskmanager.pages.TaskPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TaskTests extends BaseTest {

    private TaskPage taskPage;

    @BeforeMethod
    public void setUpTaskTests() {
        loginAsValidUser();
        taskPage = new TaskPage(driver);
    }

    @Test(description = "Verify task creation with all required fields")
    public void testCreateTaskWithValidData() {
        String title = "Test Task " + System.currentTimeMillis();
        String description = "This is a test task description";
        String dueDate = "2025-08-01";
        String priority = "High";

        taskPage.createTask(title, description, dueDate, priority);

        // Verify success message
        String successMessage = taskPage.getSuccessMessage();
        Assert.assertEquals(successMessage.trim(), "Task created successfully.",
                "Success message should confirm task creation");

        // Refresh and verify task appears in list
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(@class, 'text-lg')]")));

        Assert.assertTrue(taskPage.isTaskPresent(title),
                "Created task should appear in the task list");
    }

    @Test(description = "Verify task details are displayed correctly")
    public void testViewTaskDetails() {
        String title = "Detail Test " + System.currentTimeMillis();
        String description = "Test task for viewing details";
        String dueDate = "2025-08-01";
        String priority = "High";

        // ✅ Create task
        taskPage.createTask(title, description, dueDate, priority);
        taskPage.waitForSuccessMessage();

        // ✅ Navigate to task details
        driver.navigate().refresh();
        taskPage.clickOnTaskTitle(title); // This was the missing piece!

        // ✅ Verify all task details
        Assert.assertEquals(taskPage.getDetailTitle(), title, "Task title should match");
        Assert.assertEquals(taskPage.getDetailDescription(), description, "Task description should match");
        Assert.assertEquals(taskPage.getDetailPriority(), priority, "Task priority should match");
        Assert.assertTrue(taskPage.getDetailDueDate().contains("2025-08-01"), "Task due date should be displayed");
        Assert.assertTrue(taskPage.getDetailStatus().contains("Incomplete"), "New task should be incomplete");
    }
    @Test(description = "Verify task can be edited successfully")
    public void testEditTaskDetails() {
        // Ensure there's at least one task to edit
        String originalTitle = "Edit Test " + System.currentTimeMillis();
        taskPage.createTask(originalTitle, "Original description", "2025-08-01", "Low");
        taskPage.waitForSuccessMessage();
        driver.navigate().refresh();

        // Edit the task
        taskPage.clickEditButton();

        String updatedTitle = "Updated Task Title " + System.currentTimeMillis();
        String updatedDescription = "Updated task description";
        String updatedDueDate = "2025-08-20";
        String updatedPriority = "Medium";

        taskPage.setEditTitle(updatedTitle);
        taskPage.setEditDescription(updatedDescription);
        taskPage.setEditDueDate(updatedDueDate);
        taskPage.selectEditPriority(updatedPriority);
        taskPage.setIsComplete(true);
        taskPage.clickSaveChanges();

        // Verify changes were saved (you might need to add verification methods)
        // This would require additional methods in TaskPage to verify the updated values
    }

    @Test(description = "Verify task completion status can be toggled")
    public void testToggleTaskCompletionStatus() {
        String title = "Toggle Test " + System.currentTimeMillis();
        String description = "Task for testing status toggle";
        String dueDate = "2025-08-10";
        String priority = "Medium";

        // Create task
        taskPage.createTask(title, description, dueDate, priority);
        taskPage.waitForSuccessMessage();

        // Toggle status to complete
        By toggleStatusButton = By.xpath("//button[contains(text(), 'Mark Complete') or contains(text(), 'Mark Incomplete')]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(toggleStatusButton));
        button.click();

        // Verify status changed to complete
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//p[strong[contains(text(),'Status:')]]"), "Complete"));

        // Verify button text changed
        WebElement updatedButton = wait.until(ExpectedConditions.visibilityOfElementLocated(toggleStatusButton));
        Assert.assertTrue(updatedButton.getText().contains("Mark Incomplete"),
                "Button should show 'Mark Incomplete' for completed tasks");
    }

    @Test(description = "Verify task can be deleted successfully")
    public void testDeleteTask() {
        String title = "Delete Test " + System.currentTimeMillis();

        // Create task to delete
        taskPage.createTask(title, "Task to be deleted", "2025-08-10", "Low");
        taskPage.waitForSuccessMessage();

        driver.navigate().refresh();

        // Delete the task
        taskPage.deleteFirstTask();

        // Verify task was deleted (if you have a method to count tasks)
        // You might need to add a wait here or refresh the page to see the change
        driver.navigate().refresh();

        // This assertion would need a proper implementation of getNumberOfTasks()
    }

    // ============== NEGATIVE TESTS ==============

    @Test(description = "Verify task creation fails with empty title")
    public void testCreateTaskWithEmptyTitle() {
        String currentUrl = driver.getCurrentUrl();

        // Attempt to create task with empty title
        taskPage.submitTaskFormWithEmptyTitle("Description without title");

        // Verify user stays on same page (form validation prevents submission)
        Assert.assertEquals(driver.getCurrentUrl(), currentUrl,
                "User should remain on same page when title is empty");

        // Additional verification: check if validation message appears
        // You might want to add a method to check for validation errors
    }

    @Test(description = "Verify task creation with past due date")
    public void testCreateTaskWithPastDueDate() {
        String title = "Past Date Test " + System.currentTimeMillis();
        String description = "Task with past due date";
        String pastDate = "2020-01-01"; // Past date
        String priority = "Medium";

        taskPage.createTask(title, description, pastDate, priority);

        // A error message Should be  appear
    }

    @Test(description = "Verify task creation with very long title")
    public void testCreateTaskWithLongTitle() {
        String longTitle = "Very Long Title ".repeat(20) + System.currentTimeMillis(); // ~300 characters
        String description = "Testing long title handling";
        String dueDate = "2025-08-01";
        String priority = "Low";

        taskPage.createTask(longTitle, description, dueDate, priority);

        // Check if it's truncated, shows error, or accepts it
        try {
            taskPage.waitForSuccessMessage();
            // If successful, verify how the title is displayed
            driver.navigate().refresh();
        } catch (Exception e) {
            // If it fails, verify appropriate error handling
            System.out.println("Long title handling: " + e.getMessage());
        }
    }



}