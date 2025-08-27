package MainUtilities.CrossBrowserTesting;

import org.apache.tools.ant.taskdefs.Java;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class CrossBrowserTesting {
    WebDriver driver;
    JavascriptExecutor js;

    // Setup method to launch browser based on the parameter (Chrome or Edge)
    @Parameters("browser")
    @BeforeClass
    public void setup(String browser){
        if (browser.equalsIgnoreCase("chrome")){
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }else {
            throw new IllegalArgumentException("Invalid browser: " + browser);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // Test to fill out the contact form on the practice website
    @Test
    public void fillContactFormTest() throws InterruptedException {
        driver.get("https://testautomationpractice.blogspot.com/");

        driver.findElement(By.id("name")).sendKeys("Gulshan");
        driver.findElement(By.id("email")).sendKeys("gulshan@gmail.com");
        driver.findElement(By.id("phone")).sendKeys("9876543212");
        driver.findElement(By.id("textarea")).sendKeys("Delhi 987722, India");
        driver.findElement(By.id("male")).click();
        driver.findElement(By.id("monday")).click();
        driver.findElement(By.id("wednesday")).click();

        Thread.sleep(5000);
    }

    // Test to validate first book name from the table on the practice website
    @Test
    public void tableValidationTest() throws InterruptedException {
        driver.get("https://testautomationpractice.blogspot.com/");

        WebElement tableCell = driver.findElement(By.xpath("//table[@name='BookTable']//tr[2]/td[1]"));
        js.executeScript("arguments[0].scrollIntoView(true)", tableCell);
        String bookName = tableCell.getText();

        System.out.println("First book in table: " + bookName);
        Assert.assertEquals(bookName, "Learn Selenium", "Book name does not match expected value!");

        Thread.sleep(5000);
    }

    // Close the browser after all tests are done
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
