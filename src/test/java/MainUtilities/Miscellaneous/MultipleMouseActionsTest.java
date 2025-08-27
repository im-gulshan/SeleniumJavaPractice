package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

public class MultipleMouseActionsTest { // 18_July
    WebDriver driver;
    Actions actions;
    WebDriverWait wait;

    @BeforeClass
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void rightClickTest(){
        driver.get("https://demo.guru99.com/test/simple_context_menu.html");
        WebElement rightClickBtn = driver.findElement(By.cssSelector(".context-menu-one"));
        actions.contextClick(rightClickBtn).perform(); // Right click
    }

    @Test(priority = 2)
    public void doubleClickBtn(){
        driver.get("https://demo.guru99.com/test/simple_context_menu.html");
        WebElement doubleClickBtn =
                driver.findElement(By.xpath("//button[text()='Double-Click Me To See Alert']"));

        actions.doubleClick(doubleClickBtn).perform();

        // ✅ Handle the alert after double click
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert Text: " + alert.getText());
            alert.accept(); // Dismiss the alert
        } catch (NoAlertPresentException e) {
            System.out.println("No alert appeared.");
        }
    }

    @Test(priority = 3)
    public void dragAndDropTest(){
        driver.get("https://jqueryui.com/droppable/");
        driver.switchTo().frame(0);

        WebElement source = driver.findElement(By.id("draggable"));
        WebElement target = driver.findElement(By.id("droppable"));

        actions.dragAndDrop(source, target).perform();
    }

    @Test(priority = 4)
    public void hoverAndClickTest(){
        driver.get("https://www.amazon.in/");
        WebElement accountList = driver.findElement(By.id("nav-link-accountList"));

        actions.moveToElement(accountList).perform(); // Hover

        // Amazon sometimes dynamically loads elements, wait until it's present
        try {
            WebElement yourOrders = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.linkText("Your Orders"))
            );
            actions.moveToElement(yourOrders).click().perform();
        } catch (TimeoutException e) {
            System.out.println("Your Orders link not found.");
        }
    }

    @AfterClass
    public void tearDown(){
        if (driver != null){
            driver.quit();
        }
    }
}
