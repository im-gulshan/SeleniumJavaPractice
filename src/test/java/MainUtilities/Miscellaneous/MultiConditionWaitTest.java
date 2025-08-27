package MainUtilities.Miscellaneous;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class MultiConditionWaitTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testFourWaitConditions(){
        // Condition 1: visibilityOfElementLocated
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
        driver.findElement(By.xpath("//div[@id='start']/button")).click();
        WebElement helloWorldText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='finish']/h4")));
        System.out.println("Visible Text: " + helloWorldText.getText());

        // Condition 2: elementToBeClickable
        driver.navigate().to("https://the-internet.herokuapp.com/dynamic_loading/2");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='start']/button")));

        // Condition 3: textToBePresentInElementLocated
        driver.findElement(By.xpath("//div[@id='start']/button")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//div[@id='finish']/h4"), "Hello World!"));
        System.out.println("Text appeared successfully");

        // Condition 4: alertIsPresent
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.xpath("//button[@id='timerAlertButton']")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Alert text: " + alert.getText());
        alert.accept();
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
