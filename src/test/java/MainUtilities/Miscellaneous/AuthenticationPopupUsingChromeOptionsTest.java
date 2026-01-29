package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationPopupUsingChromeOptionsTest {

    private WebDriver driver;

    @BeforeClass
    public void setup() {

        ChromeOptions options = new ChromeOptions();

        // Disable Chrome automation infobars
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Disable password manager and credentials service
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @Test
    public void handleBasicAuthPopupUsingChromeOptions() {

        // Basic Auth credentials
        String username = "admin";
        String password = "admin";

        // Embed credentials in URL (handled by browser, no alert interaction)
        String authUrl = "https://" + username + ":" + password + "@the-internet.herokuapp.com/basic_auth";

        driver.get(authUrl);

        String successMessage = driver.findElement(By.tagName("p")).getText();
        Assert.assertTrue(successMessage.contains("Congratulations"),
                "Authentication failed or page not loaded properly");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
