package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class SoftVsHardAssertExample {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void hardAssertTest(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/");

        System.out.println("=== Hard Assert Test Started ===");

        // Hard assert → stops execution on failure
        Assert.assertTrue(driver.getTitle().contains("OrangeHRM"), "Title mismatch!");

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // If this fails, remaining code will NOT execute
        Assert.assertEquals(driver.findElement(By.xpath("//h6")).getText(),
                "Dashboard", "Not on Dashboard page!");

        System.out.println("This line will execute only if all hard asserts above passed!");
    }

    @Test(priority = 2)
    public void softAssertTest(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("=== Soft Assert Test Started ===");

        SoftAssert soft = new SoftAssert();
        driver.get("https://opensource-demo.orangehrmlive.com/");

        // Soft asserts → record failures but continue execution
        soft.assertTrue(driver.getTitle().contains("OrangeHRM123"), "Title mismatch!");  // Intentionally wrong
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        soft.assertEquals(driver.findElement(By.xpath("//h6")).getText(),
                "Dashboard123", "Dashboard text mismatch!");  // Intentionally wrong

        System.out.println("This line executes even if assertions above fail (SoftAssert).");

        // IMPORTANT → collates all failures and reports them
        soft.assertAll();
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

}
