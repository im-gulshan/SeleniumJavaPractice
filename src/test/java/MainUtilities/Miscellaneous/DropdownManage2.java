package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class DropdownManage2 {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void selectTagDropdown() throws Exception{
        driver.get("https://demo.guru99.com/test/newtours/register.php");

        // Wait for dropdown to be present instead of assuming it's there
        WebElement selectDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='country']"))
        );
        Select dropdown = new Select(selectDropdown);

        // Define test data array for cleaner logic
        String[] countries = {"INDIA", "AUSTRALIA", "NEPAL", "EGYPT"};

        // Loop through countries with proper verification logic
        for (String country : countries) {
            // Select country
            dropdown.selectByVisibleText(country);

            // Wait for selection to complete and verify
            wait.until(ExpectedConditions.textToBe(
                    By.xpath("//select[@name='country']/option[@value='"+country+"']"), country
            ));

            // Verify selection logic
            WebElement selectedElement = dropdown.getFirstSelectedOption();
            String actualSelection = selectedElement.getText();

            // Assert instead of just printing
            Assert.assertEquals(actualSelection, country,
                    "Expected " + country + " but got " + actualSelection);

            System.out.println("Successfully verified selection: " + actualSelection);
        }
    }

    @Test(priority = 2)
    public void selectOptionFromCustomDropdown() throws Exception {
        driver.get("https://www.w3schools.com/howto/howto_custom_select.asp");

        // Wait for page to fully load instead of fixed delay
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='custom-select']//div[contains(@class,'select-selected')]")
        ));

        // Click on dropdown with proper wait logic
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='custom-select']//div[contains(@class,'select-selected')]")
        ));
        dropdown.click();

        // Wait for options to be visible
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@class='custom-select']//div[contains(@class,'select-items')]/div")
        ));


        String targetOption = "Mini";
        boolean optionFound = false;

        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase(targetOption)) {
                // Ensure option is clickable before clicking
                wait.until(ExpectedConditions.elementToBeClickable(option));
                option.click();
                optionFound = true;
                break;
            }
        }

        // Verify the option was found and selected
        Assert.assertTrue(optionFound, "Target option '" + targetOption + "' was not found in dropdown");

        // Additional verification: Check if the dropdown shows selected value
        WebElement selectedDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='custom-select']//div[contains(@class,'select-selected')]")
        ));

        // Verify the displayed text matches our selection
        Assert.assertEquals(selectedDisplay.getText(), targetOption,
                "Dropdown display doesn't match selected option");

        System.out.println("Successfully selected and verified: " + targetOption);
    }

    @AfterMethod
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
}
