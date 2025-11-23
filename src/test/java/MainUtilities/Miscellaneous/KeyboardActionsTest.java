package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class KeyboardActionsTest {
    WebDriver driver;
    Actions actions;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    //    Test Case 1: Copy & Paste using Keyboard Shortcuts (CTRL + A, C, V)
    @Test
    public void testCopyPasteWithScroll() throws InterruptedException {
        driver.get("https://demoqa.com/text-box");

        WebElement fullName = driver.findElement(By.id("userName"));
        WebElement email = driver.findElement(By.id("userEmail"));

        // Scroll the first input into view
        js.executeScript("arguments[0].scrollIntoView(true);", fullName);

        fullName.sendKeys("Gulshan Kumar");

        // CTRL + A (Select all)
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();

        // CTRL + C (Copy)
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();

        // TAB (Move to next input)
        actions.sendKeys(Keys.TAB).perform();

        // CTRL + V (Paste)
        actions.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();

        Thread.sleep(2000);
    }

    //    Test Case 2: Pressing ENTER to Submit a Form
    @Test
    public void testPressEnterToSubmitForm() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");

        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));

        // Scroll username input into view
        js.executeScript("arguments[0].scrollIntoView(true);", username);

        // Enter credentials
        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");

        // Press ENTER to submit form
        actions.sendKeys(Keys.ENTER).perform();

        Thread.sleep(3000);
    }
}
