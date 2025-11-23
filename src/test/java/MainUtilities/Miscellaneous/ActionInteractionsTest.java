package MainUtilities.Miscellaneous;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ActionInteractionsTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

     // 1) Right-click (context click) and assert the context menu message.
    @Test
    public void rightClickAndHandleContextMenu() {
        driver.get("https://demoqa.com/buttons");

        // Wait for the right click button to be visible (it has id "rightClickBtn")
        WebElement rightClickBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("rightClickBtn"))
        );

        // Context click (right click) on the element
        actions.contextClick(rightClickBtn).perform();

        // After right click, demo site shows a message in id "rightClickMessage"
        WebElement rightClickMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("rightClickMessage"))
        );

        String msg = rightClickMessage.getText().trim();
        System.out.println("Right click message: " + msg);

        // Assert expected message contains "You have done a right click"
        Assert.assertTrue(msg.contains("You have done a right click"),
                "Expected right-click confirmation message not found. Actual: " + msg);
    }


     // 2) Double-click to perform action and verify result.

    @Test
    public void doubleClickPerformAction() {
        driver.get("https://demoqa.com/buttons");

        // Wait for the double click button (id "doubleClickBtn")
        WebElement doubleClickBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("doubleClickBtn"))
        );

        // Double click
        actions.doubleClick(doubleClickBtn).perform();

        // The demo shows an element with id "doubleClickMessage"
        WebElement doubleClickMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage"))
        );

        String msg = doubleClickMessage.getText().trim();
        System.out.println("Double click message: " + msg);

        // Assert expected message contains "You have done a double click"
        Assert.assertTrue(msg.contains("You have done a double click"),
                "Expected double-click confirmation message not found. Actual: " + msg);
    }

     // 3) Click-and-hold + release (drag and drop).
    @Test
    public void clickAndHoldThenRelease_DragAndDrop() {
        driver.get("https://demoqa.com/droppable");

        // Wait for draggable (id "draggable") and droppable (id "droppable")
        WebElement draggable = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("draggable"))
        );
        WebElement droppable = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("droppable"))
        );

        // Perform click-and-hold on draggable, move to droppable and release.
        actions.clickAndHold(draggable)
                .moveToElement(droppable)
                .pause(Duration.ofMillis(300)) // short pause for realism
                .release()
                .perform();

        // After drop, the droppable text changes to "Dropped!" (demoqa behavior)
        WebElement dropText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#droppable p"))
        );

        String text = dropText.getText().trim();
        System.out.println("Droppable text after drop: " + text);

        Assert.assertTrue(text.equalsIgnoreCase("Dropped!"),
                "Drag and drop did not succeed. Expected 'Dropped!' but got: " + text);
    }
}
