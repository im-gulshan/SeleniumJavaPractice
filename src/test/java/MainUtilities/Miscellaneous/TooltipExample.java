package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TooltipExample {
    WebDriver driver;
    JavascriptExecutor js;
    Actions actions;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        driver.get("https://demoqa.com/tool-tips");
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void toolTipTest() throws InterruptedException {

        WebElement toolTipElement = driver.findElement(By.xpath("//button[@id='toolTipButton']"));
        js.executeScript("arguments[0].scrollIntoView(true)", toolTipElement);

        actions.moveToElement(toolTipElement).perform();
        WebElement toolTipTextElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tooltip-inner"))
        );

        String toolTipMsg = toolTipTextElement.getText();
        System.out.println(toolTipMsg);
    }
}
