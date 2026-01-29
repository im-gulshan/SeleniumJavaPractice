package MainUtilities.ImageComparator;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;

public class VisualCompareTest {
    private WebDriver driver;

    private static final String SCREENSHOT_DIR = "screenshots";
    private static final String BASELINE_PATH  = SCREENSHOT_DIR + "/wikipedia_logo_baseline.png";
    private static final String CURRENT_PATH   = SCREENSHOT_DIR + "/wikipedia_logo_current.png";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void compareWikipediaLogo() throws Exception {
        // 1. Open a real, stable site
        driver.get("https://www.wikipedia.org/");

        // 2. Wait for the main Wikipedia globe logo to be visible
        By logoLocator = By.cssSelector("img.central-featured-logo");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(logoLocator));

        // 3. Take screenshot of that element only
        File rawScreenshot = logo.getScreenshotAs(OutputType.FILE);
        File currentImage = new File(CURRENT_PATH);
        FileUtils.copyFile(rawScreenshot, currentImage);

        File baselineImage = new File(BASELINE_PATH);

        // 4. First run – if baseline does not exist, create it from current
        if (!baselineImage.exists()) {
            System.out.println("Baseline logo image not found. Creating baseline from current screenshot.");
            FileUtils.copyFile(currentImage, baselineImage);
            return; // Pass test on first run
        }

        // 5. Compare images using ImageComparator
        double allowedDifferencePercent = 0.5; // 0.5% tolerance
        boolean isSame = ImageComparator.compareImages(baselineImage, currentImage, allowedDifferencePercent);

        // 6. Assert
        Assert.assertTrue(
                isSame,
                "Logo visual difference is more than allowed threshold of " + allowedDifferencePercent + "%"
        );
    }
}
