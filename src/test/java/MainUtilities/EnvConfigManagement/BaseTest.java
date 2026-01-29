package MainUtilities.EnvConfigManagement;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {

    protected WebDriver driver;

    @Parameters("env")
    @BeforeClass
    public void setup(String env) {

        ConfigLoader.load(env);
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get(ConfigLoader.get("base.url"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
