package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.NoSuchElementException;

public class RadioButtonTest {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
        js = (JavascriptExecutor) driver;
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSelectMaleRadioButton() throws InterruptedException {
        List<WebElement> radios = driver.findElements(By.xpath("//input[@name='gender']"));
        js.executeScript("arguments[0].scrollIntoView(true);", radios.get(0));

        // Select "male"
        selectRadioButton(radios, "male");
        Thread.sleep(2000);
    }

    @Test
    public void testSelectFemaleRadioButton() throws InterruptedException {
        List<WebElement> radios = driver.findElements(By.xpath("//input[@name='gender']"));
        js.executeScript("arguments[0].scrollIntoView(true);", radios.get(0));

        // Select "female"
        selectRadioButton(radios, "female");
        Thread.sleep(2000);
    }

    // Utility Method
    public void selectRadioButton(List<WebElement> radioButtons, String value) {
        boolean found = false;

        for (WebElement radio : radioButtons) {
            String actualValue = radio.getAttribute("value");

            if (!radio.isEnabled()) {
                System.out.println("Radio with value [" + actualValue + "] is disabled, skipping...");
                continue;
            }

            if (actualValue.equalsIgnoreCase(value)) {
                // Always click (to print consistent logs)
                radio.click();
                System.out.println("Selected radio button: " + actualValue);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("No radio button found with value: " + value);
        }
    }
}
