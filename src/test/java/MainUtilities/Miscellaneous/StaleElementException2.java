package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class StaleElementException2 {

    private WebDriver driver;

    // Locator for the "Online Training" link
    private By trainingLink = By.xpath("//a[text()='Online Training']");

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.pavantestingtools.com/");
    }

    @Test
    public void testTrainingLinkHandlesStaleElement() {
        // Go to training page and back to cause stale element
        navigateToTrainingAndBack();

        // Click with stale element handling (retry mechanism)
        safeClick(trainingLink, 2);

        // Validate expected page title
        Assert.assertEquals(driver.getTitle(), "SDET-QA",
                "After clicking Training link, page title did not match.");
    }

    // Navigate to training page and return back to previous page
    private void navigateToTrainingAndBack() {
        driver.findElement(trainingLink).click();
        driver.navigate().back();

        // Wait until link is reloaded
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(trainingLink));
    }

    // Utility method to handle StaleElementReferenceException with retries
    private void safeClick(By locator, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                driver.findElement(locator).click();
                return;
            } catch (StaleElementReferenceException e) {
                if (i == retries - 1) throw e;
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

}
