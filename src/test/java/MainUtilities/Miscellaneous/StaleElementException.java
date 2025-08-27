package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StaleElementException {
    WebDriver driver;
    String trainingXpathStr = "//a[text()='Online Training']";

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.pavantestingtools.com/");
    }

    @Test(priority = 1)
    public void staleElementExceptionWillCome() throws Exception{
        // Capture the WebElement reference before navigation
        WebElement trainingPage = driver.findElement(By.xpath(trainingXpathStr));
        trainingPage.click();

        // Navigates back to the previous page
        driver.navigate().back();
        Thread.sleep(2000);

        // This line will throw StaleElementReferenceException
        // because the original element is no longer attached to the DOM after navigation
        trainingPage.click();
    }


    @Test(priority = 2)
    public void staleElementExceptionWillNotCome() throws Exception{
        // Capture the WebElement reference before navigation
        WebElement trainingPage = driver.findElement(By.xpath(trainingXpathStr));
        trainingPage.click();

        // Navigates back to the previous page
        driver.navigate().back();
        Thread.sleep(2000);

        try{
            // Attempt to use the original element reference
            trainingPage.click();
        } catch (Exception e){
            // On StaleElementReferenceException, re-locate the element from the DOM
            // This ensures the element is always valid before interacting with it
            trainingPage = driver.findElement(By.xpath(trainingXpathStr));
            trainingPage.click();
        }

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
} // StaleElementException

