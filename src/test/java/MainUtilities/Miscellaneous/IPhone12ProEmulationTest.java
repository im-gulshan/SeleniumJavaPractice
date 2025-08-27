package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.*;

public class IPhone12ProEmulationTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Configure Chrome to emulate iPhone 12 Pro for mobile responsive testing
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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
        Thread.sleep(4000);
    }

    @AfterMethod()
    public void tearDown() {
        driver.quit();
    }
}
