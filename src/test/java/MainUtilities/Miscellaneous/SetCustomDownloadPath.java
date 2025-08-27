package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;

import java.util.*;

public class SetCustomDownloadPath {
    WebDriver driver;
    String downloadFilepath = "D:\\SeleniumDownloads";

    @BeforeMethod
    public void setup(){
        // Configure Chrome preferences to set custom download directory
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFilepath);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void testSetCustomDownloadPath() throws Exception{
        driver.get("https://demo.automationtesting.in/FileDownload.html");

        WebElement download = driver.findElement(By.xpath("//a[@type='button']"));
        download.click();

        Thread.sleep(5000);
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
