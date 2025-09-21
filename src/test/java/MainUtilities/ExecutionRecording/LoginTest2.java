package MainUtilities.ExecutionRecording;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

public class LoginTest2 extends BaseTest {
    private WebDriver driver;

    // Test data
    private static final String BASE_URL = "https://the-internet.herokuapp.com/login";
    private static final String VALID_USERNAME = "tomsmith";
    private static final String VALID_PASSWORD = "SuperSecretPassword!";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void beforeEachTest() {
        driver.get(BASE_URL);
    }

    @Test(description = "LoginTest")
    public void passwordFieldSecurityTest() throws InterruptedException {
        WebElement usernameElement = driver.findElement(By.id("username"));
        WebElement passwordElement = driver.findElement(By.id("password"));
        WebElement loginElement = driver.findElement(By.xpath("//button/i"));

        usernameElement.sendKeys(VALID_USERNAME);
        passwordElement.sendKeys(VALID_PASSWORD);
        loginElement.click();

        Thread.sleep(4000);
        Assert.fail("Intentionally Failing Test");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
