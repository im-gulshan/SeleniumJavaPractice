package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v143.emulation.Emulation;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Optional;

public class MobileEmulationTest {
    private WebDriver driver;
    private DevTools devTools;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--no-sandbox");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        devTools.send(Emulation.setDeviceMetricsOverride(
                390,   // width (mobile)
                844,   // height
                3.0,   // device scale factor
                true,  // mobile
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        devTools.send(Emulation.setUserAgentOverride(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) " +
                        "Version/17.0 Mobile/15E148 Safari/604.1",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
    }

    @Test
    public void verifyMobileUserFlow() {
        driver.get("https://www.selenium.dev");

        // Action 1: Open mobile navigation menu
        driver.findElement(By.cssSelector("button[aria-label='Toggle navigation']")).click();

        // Action 2: Navigate to Documentation
        driver.findElement(By.linkText("Documentation")).click();

        // Action 3: Scroll using touch-like interaction
        Actions actions = new Actions(driver);
        actions.scrollByAmount(0, 600).perform();

        // Action 4: Click on a mobile-visible section link
        driver.findElement(By.partialLinkText("WebDriver")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
