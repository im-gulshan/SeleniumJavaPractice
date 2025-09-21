package MainUtilities.ManageCheckBoxes;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class TestCheckBoxes {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://the-internet.herokuapp.com/checkboxes");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testCheckboxes(){
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='checkbox']")));

        List<WebElement> checkBoxes = driver.findElements(
                By.xpath("//input[@type='checkbox']"));

        System.out.println("=== Initial Checkbox States ===");
        printCheckBoxStates(checkBoxes);

        // Select all unchecked checkboxes
        System.out.println("\n=== Selecting All Checkboxes ===");
        for (int i=0; i< checkBoxes.size(); i++){
            boolean flag = checkBoxes.get(i).isSelected();

            if(!flag){
                checkBoxes.get(i).click();
                System.out.println("Checkbox " + (i + 1) + " selected");
            } else {
                System.out.println("Checkbox " + (i + 1) + " already selected");
            }
        }

        // Verify all are selected
        System.out.println("\n=== Final Checkbox States ===");
        printCheckBoxStates(checkBoxes);

        // Deselect alternate checkboxes
        System.out.println("\n=== Deselecting Alternate Checkboxes ===");
        for (int i=0; i< checkBoxes.size(); i++){
            boolean flag = checkBoxes.get(i).isSelected();

            if(flag){
                checkBoxes.get(i).click();
                System.out.println("Checkbox " + (i + 1) + " deselected");
            }
        }

        System.out.println("\n=== Final States After Alternate Deselection ===");
        printCheckBoxStates(checkBoxes);
    }

    public void printCheckBoxStates(List<WebElement> checkBoxes){
        for(int i=0; i< checkBoxes.size(); i++){
            boolean isSelected = checkBoxes.get(i).isSelected();
            System.out.println("Checkbox " + (i + 1) + ": " + (isSelected ? "✓ Selected" : "✗ Not Selected"));
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
