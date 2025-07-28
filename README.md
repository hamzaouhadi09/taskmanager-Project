# Task Manager Test Automation

A comprehensive Selenium WebDriver test automation framework for testing a task management web application. This project uses Java, TestNG, and follows the Page Object Model (POM) design pattern.

## Project Overview

This test automation framework covers end-to-end testing of a task management application including:
- User authentication (registration, login, logout)
- Task management (create, read, update, delete)
- Task status management
- Form validation testing

## 🛠️ Tech Stack

- **Java** - Programming language
- **Selenium WebDriver** - Web automation framework
- **TestNG** - Testing framework
- **WebDriverManager** - Automatic driver management
- **ExtentReports** - Test reporting
- **Maven** - Build and dependency management
- **Chrome Driver** - Browser automation

## 📁 Project Structure

```
src/
├── screenshots/     
├── main/java/
│   └── com/taskmanager/pages/
│       ├── LoginPage.java          # Login page object
│       ├── RegisterPage.java       # Registration page object
│       ├── DashboardPage.java      # Dashboard page object
│       └── TaskPage.java           # Task management page object
└── test/java/
|    └── com/hamza/taskmanager/
|        ├── BaseTest.java           # Base test configuration
|        ├── AuthTests.java          # Authentication test cases
|        └── TaskTests.java          # Task management test cases
│           └── utils/
│               ├── ExtentReportManager.java    # Gestionnaire des rapports Extent
│               ├── ExtentTestListener.java     # Listener pour les rapports
│        
|
├── pom.xml                         # Configuration Maven
├── testng.xml                      # Configuration TestNG




##  Test Coverage

### Authentication Tests (`AuthTests.java`)
- ✅ User registration with valid credentials
- ✅ Registration success message validation
- ✅ Registration with short password (negative test)
- ✅ Registration with existing email (negative test)
- ✅ Successful login with valid credentials
- ✅ Login with invalid credentials (negative test)
- ✅ User logout functionality

### Task Management Tests (`TaskTests.java`)
- ✅ Create task with all required fields
- ✅ View task details
- ✅ Edit task details
- ✅ Toggle task completion status
- ✅ Delete task
- ✅ Create task with empty title (negative test)
- ✅ Create task with past due date (negative test)
- ✅ Create task with very long title (negative test)

## 🔧 Setup and Installation

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Chrome browser
- Git

### Installation Steps

1. **Clone the repository**
   bash
   git clone https://github.com/hamzaouhadi09/taskmanager-Project
   cd taskmanager-Project
   

2. **Install dependencies**
    bash
   mvn clean install
   
3. **Run tests**
    bash
   # Run all tests
   mvn test
