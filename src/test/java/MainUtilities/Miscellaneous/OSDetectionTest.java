package MainUtilities.Miscellaneous;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OSDetectionTest {

    WebDriver driver;
    String osName;

    // -------- OS Detection Methods --------
    public boolean isWindows() {
        return osName.contains("win");
    }

    public boolean isMac() {
        return osName.contains("mac");
    }

    public boolean isLinux() {
        return osName.contains("nux") || osName.contains("nix");
    }

    // -------- TestNG Setup --------
    @BeforeClass
    public void setup() {

        osName = System.getProperty("os.name").toLowerCase();
        System.out.println("Detected OS: " + osName);

        if (isMac()) {
            System.out.println("Launching Safari on Mac");
            driver = new SafariDriver();   // Safari only works on Mac
        }
        else if (isWindows()) {
            System.out.println("Launching Edge on Windows");
            driver = new EdgeDriver();     // Selenium Manager handles driver
        }
        else {
            System.out.println("Launching Chrome on Linux/Other OS");
            driver = new ChromeDriver();   // Selenium Manager handles driver
        }

        driver.manage().window().maximize();
    }

    // -------- Test Case --------
    @Test
    public void verifyOSDetection() {

        Assert.assertNotNull(driver, "Driver should be initialized");

        if (isWindows()) {
            System.out.println("Test running on Windows OS");
        }
        else if (isMac()) {
            System.out.println("Test running on Mac OS");
        }
        else if (isLinux()) {
            System.out.println("Test running on Linux OS");
        }
        else {
            Assert.fail("Unsupported OS detected");
        }
    }

    // -------- Tear Down --------
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
