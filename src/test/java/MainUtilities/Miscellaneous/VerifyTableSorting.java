package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerifyTableSorting {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/tables");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void verifyNameColumnSorting() {

        // Step 11: Locate the "Last Name" column header and click to sort
        WebElement nameHeader = driver.findElement(By.xpath("//table[@id='table1']//span[text()='Last Name']"));
        nameHeader.click();

        // Step 2️: Capture all names in the "Last Name" column after clicking
        List<WebElement> nameElements = driver.findElements(By.xpath("//table[@id='table1']/tbody/tr/td[1]"));
        List<String> actualNames = new ArrayList<>();

        for (WebElement e : nameElements) {
            actualNames.add(e.getText().trim());
        }

        // Step 3️: Copy the list and sort it manually (for comparison)
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        System.out.println("Actual Names (UI Sorted):   " + actualNames);
        System.out.println("Expected Names (Manually Sorted): " + expectedNames);

        // Step 4️: Verify both lists are equal
        Assert.assertEquals(actualNames, expectedNames, "❌ Sorting verification failed!");

        System.out.println("Table is sorted correctly by 'Last Name'.");
    }
}
