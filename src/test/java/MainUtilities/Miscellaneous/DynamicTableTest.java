package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DynamicTableTest { //20_july
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void driverSetup(){
        // Initialize Chrome browser and navigate to the dynamic table page
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practice.expandtesting.com/dynamic-table");
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void manageDynamicTable() throws Exception{
        // Capture all header columns of the dynamic table
        List<WebElement> tableHeader = driver.findElements(By.xpath("//tr/th"));

        // Scroll to the table header to make sure it's in view
        js.executeScript("arguments[0].scrollIntoView(true)", tableHeader.get(0));

        // Define expected headers for validation
        List<String> expectedHeader = Arrays.asList("Name", "CPU", "Network", "Disk", "Memory");

        // Verify each header in the UI exists in the expected list
        for (WebElement webElement : tableHeader) {
            String currentHeader = webElement.getText();
            if (!expectedHeader.contains(currentHeader)) {
                Assert.fail(currentHeader + ", is not found on UI");
                break;
            }
        }

        System.out.println("Expected column matched with Actual column");

        // Get all browser names from the first column of the table
        List<WebElement> allBrowser = driver.findElements(By.xpath("//tbody/tr/td[1]"));
        boolean check = true;

        // Look for "Chrome" in the list of browser names
        for (WebElement webElement : allBrowser) {
            String currentBrowser = webElement.getText();
            if (currentBrowser.equalsIgnoreCase("Chrome")) {
                System.out.println("Chrome browser is found on Task Manger Table");
                check = false;
                break;
            }
        }

        // If Chrome is not found, fail the test
        if (check){
            System.out.println("Chrome doesn't found in Task Manager Table");
            Assert.fail("Chrome doesn't found in Task Manager Table");
        }

        // Get all performance data (excluding "Name") for the Chrome browser row
        List<WebElement> browserData = driver.findElements(By.xpath("//tbody/tr/td[(text()='Chrome')]/following-sibling::td"));

        // Print Chrome's performance data with its column name
        for (int i=0; i<browserData.size(); i++){
            String data = browserData.get(i).getText();
            String columnName = tableHeader.get(i+1).getText();

            System.out.println("Chrome "+columnName+" : "+data);
        }
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
