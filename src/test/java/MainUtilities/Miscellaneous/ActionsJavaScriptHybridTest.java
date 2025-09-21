package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ActionsJavaScriptHybridTest {
    WebDriver driver;
    Actions actions;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeClass
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void jsScrollHoverAndClickTest() {
        driver.get("https://www.amazon.in/");
        WebElement sellOnAmazon = driver.findElement(By.xpath("//a[text()='Sell on Amazon']"));

        // Scroll into view with JS
        js.executeScript("arguments[0].scrollIntoView(true);", sellOnAmazon);

        // Hover with Actions
        actions.moveToElement(sellOnAmazon).perform();

        // Click with JS (in case normal click fails)
        js.executeScript("arguments[0].click();", sellOnAmazon);
    }

    @Test(priority = 2)
    public void jsScrollAndDoubleClickTest() throws InterruptedException {
        driver.get("https://demo.guru99.com/test/simple_context_menu.html");
        WebElement doubleClickBtn = driver.findElement(By.xpath("//button[text()='Double-Click Me To See Alert']"));

        //Scroll into view with JS
        js.executeScript("arguments[0].scrollIntoView(true);", doubleClickBtn);

        //Double-click with Actions
        actions.doubleClick(doubleClickBtn).perform();

        //Handle alert
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert Text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert appeared after double click.");
        }
    }


    @AfterClass
    public void tearDown(){
        driver.quit();
    }

}
