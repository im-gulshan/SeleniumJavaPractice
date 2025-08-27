package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class IncognitoTest {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        // Configure Chrome to run in incognito mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito"); // Use new incognito mode

        // Initialize Chrome driver with incognito configuration
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void printAllProduct() throws Exception{
        // Login to the application
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        // Enter valid credentials and submit login form
        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        login.click();

        // Get all product names from the inventory page
        List<WebElement> productLists = driver.findElements(By.xpath("//div[@class='inventory_item_name ']"));

        // Print each product name to console
        for (WebElement productList : productLists){
            String productName = productList.getText();
            System.out.println(productName);
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}