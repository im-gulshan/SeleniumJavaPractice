package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class WaitForURLChangeDemo {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    //    Test Case 1: Wait for URL change after clicking a link
    @Test
    public void testWaitForURLAfterLinkClick() {
        // Open home page
        driver.get("https://the-internet.herokuapp.com/");

        // Click "Form Authentication" link
        driver.findElement(By.linkText("Form Authentication")).click();

        // Wait until URL contains "/login"
        wait.until(ExpectedConditions.urlContains("/login"));

        System.out.println("URL after clicking link: " + driver.getCurrentUrl());
    }

    //    Test Case 2: Wait for URL change after login submission    @Test
    @Test
    public void testWaitForURLAfterLogin() {
        // Open login page directly
        driver.get("https://the-internet.herokuapp.com/login");

        // Enter credentials
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        // Click login button
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait until URL contains "/secure" after successful login
        wait.until(ExpectedConditions.urlContains("/secure"));

        System.out.println("URL after login: " + driver.getCurrentUrl());
    }
}
