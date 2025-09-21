package MainUtilities.SelfHealingTechnique;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoLogin {

    private WebDriver driver;
    private SelfHealingLocator healer;

    @BeforeMethod
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        healer = new SelfHealingLocator(driver);

        // IMPROVED: Reasonable implicit wait (was 300 seconds)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSauceDemo() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");

        // Using self-healing locators - the login button XPath is intentionally broken to test healing
        WebElement usernameElement = healer.findElement("//input[@id = 'user-name']");
        WebElement passwordElement = healer.findElement("//input[@id = 'password']");

        // Intentionally broken (should be 'login-button')
        WebElement loginElement = healer.findElement("//input[@id = 'login-button2']");

        usernameElement.sendKeys("standard_user");
        passwordElement.sendKeys("secret_sauce");
        loginElement.click();

        // Verify successful login by checking for logo
        WebElement logoElement = healer.findElement("//div[@class= 'app_logo']");
        String logoText = logoElement.getText();

        System.out.println("Logo Text: " + logoText);

        // Optional: Add assertion to verify login success
        assert logoText.contains("Swag Labs") : "Login failed - logo not found or incorrect";
        System.out.println("Login test completed successfully!");

        Thread.sleep(4000);
    }

    @AfterMethod
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
}
