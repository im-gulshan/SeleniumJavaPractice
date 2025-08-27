package MainUtilities.RetryMechanism;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// Demo test class to showcase retry mechanism functionality
public class SampleRetryTest {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Navigate to demo login page
        driver.get("https://www.saucedemo.com/");
    }

    // Attach retry analyzer to this test
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void flakyTest(){
        // Locate login form elements
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        // Perform login actions
        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        login.click();

        // Force failure to test retry mechanism
        Assert.fail("Failing this test intentionally");
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}