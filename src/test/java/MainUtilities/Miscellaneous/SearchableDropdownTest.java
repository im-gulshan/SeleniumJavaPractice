package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class SearchableDropdownTest {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://select2.org/getting-started/basic-usage");
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void test_SearchAndSelectDropdown() {

        // Locate dropdown using XPath and scroll into view
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[contains(@class,'select2-selection')])[1]")
        ));

        js.executeScript("arguments[0].scrollIntoView(true);", dropdown);
        dropdown.click(); // open dropdown

        // Locate search input box of Select2 dropdown via XPath
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@class='select2-search__field']")
        ));
        searchBox.sendKeys("California");

        // Wait for the dropdown result option and click it
        WebElement desiredOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(@class,'select2-results__option') and normalize-space(text())='California']")
        ));
        desiredOption.click();

        // Validate selected value (again using XPath)
        WebElement selectedValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//span[contains(@class,'select2-selection__rendered')])[1]")
        ));

        String actualText = selectedValue.getText().trim();
        Assert.assertEquals(actualText, "California", "Dropdown selected value is incorrect!");

        System.out.println("Selected value is: " + actualText);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
