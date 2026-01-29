package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SamePriorityWithDependencyTest {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.out.println("Browser Launched");
    }

    @Test(priority = 1)
    public void login() {
        driver.get("https://www.google.com");

        // Deliberate wrong validation to force failure
        Assert.assertTrue(
                driver.getTitle().contains("Facebook"),
                "Login failed: Expected Facebook page but Google loaded"
        );

        System.out.println("login executed");
    }

    @Test(priority = 1, dependsOnMethods = "login")
    public void searchProduct() {
        // Will never run because login() fails
        System.out.println("searchProduct executed");
    }


    @Test(priority = 1)
    public void openGmail() {
        driver.get("https://mail.google.com");

        Assert.assertTrue(
                driver.getCurrentUrl().contains("mail.google.com"),
                "Gmail page not opened"
        );

        System.out.println("openGmail executed");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        System.out.println("Browser Closed");
    }

}
