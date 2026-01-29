package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;

public class BookingWorkflow_PageSourceCapture {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    // --------------------------------------------------------------------
    // SCENARIO 1: FAIL → page source captured and saved
    // --------------------------------------------------------------------
    @Test
    public void failureScenario_storePageSource() throws IOException {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("wrong");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("wrong");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        boolean successMessageVisible;
        try {
            successMessageVisible = driver.findElement(By.xpath("//div[contains(text(),'You logged into a secure area!')]")).isDisplayed();
        } catch (Exception e) {
            successMessageVisible = false;
        }

        if (!successMessageVisible) {
            String pageSource = driver.getPageSource();
            String filePath = System.getProperty("user.dir") + "/FAIL_login_page_source.html";

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(pageSource);
            }

            Assert.fail("Success message missing → Page source saved.");
        }
    }

    // --------------------------------------------------------------------
    // SCENARIO 2: PASS → page source NOT stored
    // --------------------------------------------------------------------
    @Test
    public void successScenario_noStore() {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("tomsmith");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        boolean successMessageVisible = driver.findElement(By.xpath("//div[contains(text(),'You logged into a secure area!')]")).isDisplayed();

        Assert.assertTrue(successMessageVisible, "Expected success message missing.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
