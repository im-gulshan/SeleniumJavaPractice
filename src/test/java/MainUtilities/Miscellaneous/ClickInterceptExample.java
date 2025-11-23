package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

public class ClickInterceptExample {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        driver.get("https://demoqa.com/modal-dialogs");
    }

    // Test 1:
     // Direct click -> ElementClickInterceptedException
    @Test
    public void test_ClickFailsDueToOverlay() {
        // Click to open large modal (overlay)
        driver.findElement(By.id("showLargeModal")).click();

        // TRY to click small modal button while overlay is still present → fails
        WebElement smallModalBtn = driver.findElement(By.id("showSmallModal"));

        // This will throw ElementClickInterceptedException
        smallModalBtn.click();
    }

     // Test 2:
     // Resolve using wait + scrollIntoView + proper click
    @Test
    public void test_ClickAfterOverlayRemoved() {

        // 1. Open large modal which blocks clicks
        driver.findElement(By.id("showLargeModal")).click();

        // 2. Close modal to remove overlay
        WebElement closeBtn = driver.findElement(By.id("closeLargeModal"));
        wait.until(ExpectedConditions.elementToBeClickable(closeBtn)).click();

        // 3. Now overlay is gone -> Click small modal button safely
        WebElement smallModalBtn = driver.findElement(By.id("showSmallModal"));

        // Scroll into view (optional but recommended)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", smallModalBtn);

        wait.until(ExpectedConditions.elementToBeClickable(smallModalBtn)).click();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
