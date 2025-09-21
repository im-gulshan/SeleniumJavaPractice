package tests;

import MainUtilities.ExecutionRecording.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

public class LoginTest extends BaseTest {

    private static final Logger logger = Logger.getLogger(LoginTest.class.getName());
    private WebDriver driver;
    private WebDriverWait wait;

    // Test data
    private static final String BASE_URL = "https://the-internet.herokuapp.com/login";
    private static final String VALID_USERNAME = "tomsmith";
    private static final String VALID_PASSWORD = "SuperSecretPassword!";
    private static final String INVALID_USERNAME = "wronguser";
    private static final String INVALID_PASSWORD = "wrongpass";

    @BeforeClass
    public void setUp() {
        logger.info("Setting up WebDriver for LoginTest");

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // For CI/CD environments, uncomment the next line
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        logger.info("WebDriver setup completed");
    }

    @BeforeMethod
    public void beforeEachTest() {
        logger.info("Navigating to login page");
        driver.get(BASE_URL);

        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
    }

    @Test(description = "Verify successful login with valid credentials", enabled = false)
    public void successfulLoginTest() {
        logger.info("Executing successful login test");

        try {
            // Enter valid credentials
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
            usernameField.clear();
            usernameField.sendKeys(VALID_USERNAME);

            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys(VALID_PASSWORD);

            // Click login button
            WebElement loginButton = driver.findElement(By.className("radius"));
            loginButton.click();

            // Wait for and verify successful login
            WebElement flashMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));
            String successMessage = flashMessage.getText();

            logger.info("Flash message: " + successMessage);

            // Verify success message
            Assert.assertTrue(successMessage.contains("You logged into a secure area!"),
                    "Expected success message not found. Actual: " + successMessage);

            // Verify we're on the secure page
            Assert.assertTrue(driver.getCurrentUrl().contains("/secure"),
                    "Not redirected to secure page");

            // Verify logout button is present
            WebElement logoutButton = driver.findElement(By.linkText("Logout"));
            Assert.assertTrue(logoutButton.isDisplayed(), "Logout button not visible");

            logger.info("✅ Successful login test completed successfully");

        } catch (Exception e) {
            logger.severe("❌ Successful login test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify login failure with invalid credentials", enabled = false)
    public void failedLoginTest() {
        logger.info("Executing failed login test");

        try {
            // Enter invalid credentials
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
            usernameField.clear();
            usernameField.sendKeys(INVALID_USERNAME);

            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys(INVALID_PASSWORD);

            // Click login button
            WebElement loginButton = driver.findElement(By.className("radius"));
            loginButton.click();

            // Wait for and verify error message
            WebElement flashMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));
            String errorMessage = flashMessage.getText();

            logger.info("Flash message: " + errorMessage);

            // Verify error message (this assertion will pass)
            Assert.assertTrue(errorMessage.contains("Your username is invalid!"),
                    "Expected error message not found. Actual: " + errorMessage);

            // Verify we're still on login page
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                    "Should remain on login page after failed login");

            logger.info("✅ Failed login test completed successfully");

        } catch (Exception e) {
            logger.severe("❌ Failed login test encountered error: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify login with empty credentials",
            expectedExceptions = AssertionError.class, enabled = false)
    public void emptyCredentialsTest() {
        logger.info("Executing empty credentials test");

        try {
            // Leave fields empty and click login
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("radius")));
            loginButton.click();

            // This should show an error message
            WebElement flashMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));
            String errorMessage = flashMessage.getText();

            logger.info("Flash message: " + errorMessage);

            // This assertion will fail intentionally to demonstrate recording
            Assert.assertTrue(errorMessage.contains("Login successful"),
                    "This test is designed to fail for recording demonstration");

        } catch (Exception e) {
            logger.severe("❌ Empty credentials test failed as expected: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify password field security (masked input)")
    public void passwordFieldSecurityTest() throws InterruptedException {
        logger.info("Executing password field security test");

        try {
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));

            // Verify password field type
            String fieldType = passwordField.getAttribute("type");
            Assert.assertEquals(fieldType, "password",
                    "Password field should be of type 'password'");

            // Enter password and verify it's masked
            passwordField.sendKeys("testpassword");
            String displayedValue = passwordField.getAttribute("value");

            // The value should be there but visually masked (this depends on browser behavior)
            Assert.assertNotNull(displayedValue, "Password field should accept input");

            logger.info("✅ Password field security test completed successfully");

        } catch (Exception e) {
            logger.severe("❌ Password field security test failed: " + e.getMessage());
            throw e;
        }

        Thread.sleep(4000);

        Assert.fail("Intentionally Failing the test");
    }

    @AfterMethod
    public void afterEachTest() {
        // Clear any state between tests
        if (driver != null) {
            try {
                // If we're on the secure page, logout first
                if (driver.getCurrentUrl().contains("/secure")) {
                    WebElement logoutButton = driver.findElement(By.linkText("Logout"));
                    logoutButton.click();
                    wait.until(ExpectedConditions.urlContains("/login"));
                }
            } catch (Exception e) {
                logger.warning("Could not logout cleanly: " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down WebDriver");

        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.warning("Error closing WebDriver: " + e.getMessage());
            }
        }
    }
}
