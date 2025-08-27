package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DropdownManage {
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

        // Initialize the Select class with the located dropdown element
        WebElement selectDropdown = driver.findElement(By.xpath("//select[@name='country']"));
        Select dropdown = new Select(selectDropdown);

        // Select India and verify if its selected
        dropdown.selectByVisibleText("INDIA");
        WebElement selectedElement = dropdown.getFirstSelectedOption();
        System.out.println("Selected country : "+selectedElement.getText());

        // Select Australia and verify if its selected
        dropdown.selectByVisibleText("AUSTRALIA");
        selectedElement = dropdown.getFirstSelectedOption();
        System.out.println("Selected country : "+selectedElement.getText());

        // Select Nepal and verify if its selected
        dropdown.selectByVisibleText("NEPAL");
        selectedElement = dropdown.getFirstSelectedOption();
        System.out.println("Selected country : "+selectedElement.getText());

        // Select Egypt and verify if its selected
        dropdown.selectByVisibleText("EGYPT");
        selectedElement = dropdown.getFirstSelectedOption();
        System.out.println("Selected country : "+selectedElement.getText());
    }

    @Test(priority = 2)
    public void selectOptionFromCustomDropdown() throws Exception {
        driver.get("https://www.w3schools.com/howto/howto_custom_select.asp");

        // Wait for page to load
        Thread.sleep(2000);

        // Click on the custom dropdown to open the options
        WebElement dropdown = driver.findElement(By.xpath("//div[@class='custom-select']//div[contains(@class,'select-selected')]"));
        dropdown.click();

        // Wait for options to be visible
        Thread.sleep(1000);

        // Get all options from dropdown
        List<WebElement> options = driver.findElements(By.xpath("//div[@class='custom-select']//div[contains(@class,'select-items')]/div"));

        // Select a specific option
        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase("Mini")) {
                option.click();
                break;
            }
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
