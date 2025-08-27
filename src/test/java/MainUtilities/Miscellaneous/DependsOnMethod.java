package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DependsOnMethod {
    WebDriver driver;

    @BeforeTest
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    // First test - opens the login page (priority = 1)
    @Test(priority = 1)
    public void openLoginPage(){
        driver.get("https://opensource-demo.orangehrmlive.com/");

        String title = driver.getTitle();
        System.out.println("Login page title : "+title);

        Assert.assertTrue(title.contains("OrangeHRM"), "Login page did not open correctly");
    }

    // Second test - performs login  (priority = 2)
    @Test(priority = 2)
    public void loginTest() throws InterruptedException {
        Thread.sleep(2000);

        // Enter username and password, then click login button
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(2000);

        // Verify successful login by checking dashboard text
        String dashboardText = driver.findElement(By.xpath("//h6")).getText();
        System.out.println("Dashboard Text: " + dashboardText);

        Assert.assertEquals(dashboardText, "Dashboard", "Login failed!");
    }

    // Third test - performs logout (priority = 3)
    @Test(priority = 3)
    public void logoutTest() throws InterruptedException {
        Thread.sleep(2000);

        // Click on user dropdown menu
        driver.findElement(By.xpath("//p[contains(@class, 'userdropdown')]")).click();

        Thread.sleep(2000);

        // Click logout link
        driver.findElement(By.linkText("Logout")).click();

        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("After Logout URL: " + currentUrl);

        // Verify logout by checking if URL contains 'login'
        Assert.assertTrue(currentUrl.contains("login"), "Logout failed!");
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }
}
