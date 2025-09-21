package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.emulation.Emulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class GeoLocationTest {
    public static void main(String[] args) {
        // Setup driver
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1); // 1 = Allow
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);

        // Create DevTools session
        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();

        // Fake location: San Francisco
        devTools.send(Emulation.setGeolocationOverride(
                Optional.of(37.7749),      // Latitude
                Optional.of(-122.4194),   // Longitude
                Optional.of(1.0),         // Accuracy
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        // Open location site
        driver.get("https://my-location.org/");

        // wait for location fetch
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        // Extract location text
        String location = driver.findElement(By.id("address")).getText();
        System.out.println("Simulated Location: " + location);

        driver.quit();
    }
}
