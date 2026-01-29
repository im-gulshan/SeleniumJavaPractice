package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v143.browser.Browser;
import org.openqa.selenium.devtools.v143.browser.model.PermissionType;
import org.openqa.selenium.devtools.v143.emulation.Emulation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrowserPermissionsTest {
    private WebDriver driver;
    private DevTools devTools;

    @BeforeMethod
    public void setup() {

        // Stable browser-level auto-allow (works even if CDP fails)
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 1);
        prefs.put("profile.default_content_setting_values.geolocation", 1);
        prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
        prefs.put("profile.default_content_setting_values.media_stream_mic", 1);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-gpu", "--no-sandbox");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Selenium 4.x correct CDP signature (7 params)
        devTools.send(Emulation.setGeolocationOverride(
                Optional.of(28.6139),
                Optional.of(77.2090),
                Optional.of(50.0),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        // Correct strongly-typed PermissionType usage
        devTools.send(Browser.grantPermissions(
                List.of(
                        PermissionType.GEOLOCATION,
                        PermissionType.NOTIFICATIONS
                ),
                Optional.of("https://permission.site"),
                Optional.empty()
        ));
    }

    @Test
    public void handleBrowserPermissionsAutomatically() {
        driver.get("https://permission.site/");

        driver.findElement(By.xpath("//button[contains(text(),'Notifications')]")).click();
        driver.findElement(By.xpath("//button[contains(text(),'Location')]")).click();
        driver.findElement(By.xpath("//button[contains(text(),'Camera')]")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
