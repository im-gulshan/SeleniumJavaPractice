package MainUtilities.TakeScreenshotOnTestFailure;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ScreenshotTest extends BaseTest{

    @Test
    public void testScreenshot(){
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
}
