package MainUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

public class HandleMultipleWindowAndTabs {
    WebDriver driver;
    JavascriptExecutor js;


    @BeforeMethod
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/browser-windows");
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void handleMultipleTabs() throws Exception{
        WebElement newTab = driver.findElement(By.xpath("//button[@id='tabButton']"));
        js.executeScript("arguments[0].scrollIntoView(true)", newTab);

        String parentWindow = driver.getWindowHandle();
        newTab.click();
        Set<String> allTabs = driver.getWindowHandles();
        for (String allTab : allTabs){
            if (!allTab.equals(parentWindow)){
                driver.switchTo().window(allTab);
                System.out.println(driver.getCurrentUrl());
                driver.close();
            }
        }
        driver.switchTo().window(parentWindow);
    }

    @Test(priority = 2)
    public void handleMultipleWindow(){
        WebElement newWindow = driver.findElement(By.xpath("//button[@id='messageWindowButton']"));
        js.executeScript("arguments[0].scrollIntoView(true)", newWindow);

        String parentWindow = driver.getWindowHandle();
        newWindow.click();
        Set<String> allTabs = driver.getWindowHandles();
        for (String allTab : allTabs){
            if (!allTab.equals(parentWindow)){
                driver.switchTo().window(allTab);
                driver.close();
            }
        }
        driver.switchTo().window(parentWindow);
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
} //HandleMultipleWindowAndTabs
