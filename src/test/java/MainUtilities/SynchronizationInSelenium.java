package MainUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class SynchronizationInSelenium {
    @Test
    public void testWaits() { //12 _july
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.instagram.com/");

        // implicitly wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Explicitly Wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element =  driver.findElement(By.xpath("//input[@name='username']"));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys("Gulshan");

        driver.quit();
    }


}
