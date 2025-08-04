package MainUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IFrameManage { //23_July
    WebDriver driver;
    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demo.automationtesting.in/Frames.html");
    }

    @Test
    public void testNestedIframe() throws Exception{

        WebElement iframe = driver.findElement(By.xpath("//a[contains(@href, 'Multiple')]"));
        // Click on the tab to open multiple/nested iframes
        iframe.click();

        WebElement nestedFrames = driver.findElement(By.xpath("//iframe[contains(@src, 'MultipleFrames')]"));
        // Switch to the outer iframe
        driver.switchTo().frame(nestedFrames);

        WebElement iframeDemo = driver.findElement(By.xpath("//iframe[@src='SingleFrame.html']"));
        // Switch to the inner iframe inside the outer one
        driver.switchTo().frame(iframeDemo);

        WebElement input = driver.findElement(By.xpath("//input[@type='text']"));
        // Enter text into the input field inside the inner iframe
        input.sendKeys("Gulshan");

        // Switch back to the main page content
        driver.switchTo().defaultContent();
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
} // IFrameManage
