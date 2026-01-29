package MainUtilities.Miscellaneous;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v142.network.Network;
import org.openqa.selenium.devtools.v142.network.model.RequestPattern;
import org.openqa.selenium.devtools.v142.network.model.ResourceType;
import org.testng.annotations.*;

import java.util.List;
import java.util.Optional;

public class BlockAdsAndImagesUsingCDP {
    WebDriver driver;
    DevTools devTools;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);

        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        try {
            // Enable network interception
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()
            , Optional.empty(), Optional.empty()));

            // Method 1: Block using Request Patterns (Recommended)
            List<RequestPattern> patterns = List.of(
                    new RequestPattern(Optional.of("*.png"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*.jpg"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*.jpeg"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*.gif"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*.webp"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*doubleclick.net*"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*googlesyndication.com*"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*ads*"), Optional.empty(), Optional.empty()),
                    new RequestPattern(Optional.of("*facebook.com/tr*"), Optional.empty(), Optional.empty())
            );

            devTools.send(Network.setBlockedURLs(patterns.stream()
                    .map(pattern -> pattern.getUrlPattern().orElse(""))
                    .toList()));

            System.out.println("Network blocking enabled successfully");

        } catch (Exception e) {
            System.err.println("Error setting up network blocking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void openHeavyWebsiteWithoutAds() throws InterruptedException {
        driver.get("https://automationexercise.com/");
        Thread.sleep(20000);
        System.out.println("Page loaded with ads & images blocked");

        // Add a small wait to observe the results
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        if (devTools != null) {
            try {
                devTools.close();
            } catch (Exception e) {
                System.err.println("Error closing DevTools: " + e.getMessage());
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}