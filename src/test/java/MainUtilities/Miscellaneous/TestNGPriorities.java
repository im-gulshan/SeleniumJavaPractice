package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.Duration;

public class TestNGPriorities {
    WebDriver driver;

    @BeforeClass
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void openApplication(){
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("Opened Application");
    }

    @Test(priority = 2)
    public void loginTest(){
        WebElement usernameElement = driver.findElement(By.id("username"));
        WebElement passwordElement = driver.findElement(By.id("password"));
        WebElement loginElement = driver.findElement(By.xpath("//button"));

        usernameElement.sendKeys("tomsmith");
        passwordElement.sendKeys("SuperSecretPassword!");
        loginElement.click();

        System.out.println("Login Successful");
    }

    @Test(priority = 3)
    private void printWelcomeMsg(){
        WebElement wlcmElementMsg = driver.findElement(By.xpath("//h4"));
        String wlcmMsg = wlcmElementMsg.getText();
        System.out.println(wlcmMsg);

        System.out.println("Welcome message printed");
    }

    @Test(priority = 4)
    private void logout(){
        WebElement logoutElement = driver.findElement(By.xpath("//a/i"));
        logoutElement.click();
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
