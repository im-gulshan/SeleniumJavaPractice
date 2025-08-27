package MainUtilities.RetryMechanism;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.epam.healenium.driver.InitDriver.getDriver;

@Listeners(RetryListener.class)
public class SampleRetryTest2 {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void flakyTest() {

        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        login.click();


        Assert.fail("Failing this test intentionally");
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
