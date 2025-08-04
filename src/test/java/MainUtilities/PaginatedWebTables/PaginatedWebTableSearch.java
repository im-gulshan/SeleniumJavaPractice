package MainUtilities.PaginatedWebTables;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class PaginatedWebTableSearch {
    WebDriver driver;

    @BeforeMethod
    public void setDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://datatables.net/examples/basic_init/table_sorting.html");
    }

    @Test
    public void verifyPaginatedData() throws InterruptedException {
        // Set target position and office location to search
        String position = "Software Engineer";
        String office = "London";

        // Get all pagination buttons on the table
        List<WebElement> paginatedButton = driver.findElements(By.xpath("//button[@class ='dt-paging-button']"));

        // Iterate through each pagination page
        for (int i = 1; i < paginatedButton.size(); i++) {

            // Search the required record in the current page
            boolean matchPosition = findPositionInTable(position, office);

            // If not found in the current page, go to the next page
            if(!matchPosition){
                WebElement nextButton = driver.findElement(By.xpath("//button[@aria-label='Next']"));
                String nextClass = nextButton.getAttribute("class");
                if(!nextClass.contains("disabled")){
                    nextButton.click();
                }
            }

        }
    }

    // Check if the required position and office location exist on the current page
    public boolean findPositionInTable(String position, String office) {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='example']//tbody/tr"));
        for (int i=0; i< rows.size(); i++) {
            WebElement row = rows.get(i);
            String currentRowText = row.getText();

            // Match position and office in the current row
            if (currentRowText.contains(position) && currentRowText.contains(office)) {
                System.out.println("Found : " + position + " with office : " + office);
                System.out.println("Name: " + driver.findElement(By.xpath("//table[@id='example']/tbody/tr[" + (i+1) + "]/td[1]")).getText());
                System.out.println("Position: " + position);
                System.out.println("Office: " + driver.findElement(By.xpath("//table[@id='example']/tbody/tr[" + (i+1) + "]/td[3]")).getText());
                System.out.println("Age: " + driver.findElement(By.xpath("//table[@id='example']/tbody/tr[" + (i+1) + "]/td[4]")).getText());
                System.out.println("Start date: " + driver.findElement(By.xpath("//table[@id='example']/tbody/tr[" + (i+1) + "]/td[5]")).getText());
                System.out.println("Salary: " + driver.findElement(By.xpath("//table[@id='example']/tbody/tr[" + (i+1) + "]/td[6]")).getText());
                return true;
            }
        }
        return false;
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

} //PaginatedWebTableSearch
