package MainUtilities.Miscellaneous;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SamePriorityExecutionTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.out.println("Browser Launched");
    }

    // Method name starts with 'a' → runs first
    @Test(priority = 1)
    public void addToCart() {
        driver.get("https://google.com");
        System.out.println("addToCart executed");
    }

    // Method name starts with 'p' → runs last
    @Test(priority = 1)
    public void payment() {
        System.out.println("payment executed");
    }

    // Method name starts with 'l' → runs second
    @Test(priority = 1)
    public void login() {
        System.out.println("login executed");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        System.out.println("Browser Closed");
    }
}
