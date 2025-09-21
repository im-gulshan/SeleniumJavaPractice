package MainUtilities.ScrollingDropDown;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ScrollingDropDown {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://testautomationpractice.blogspot.com/");
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testScrollingDropdown(){
        String selectOption = "Item 90";

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement dropDown = driver.findElement(By.xpath("//input[@id='comboBox']"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropDown);
        dropDown.click();

        // Fetch all available options from the dropdown
        List<WebElement> dropDownOptions = driver.findElements(By.xpath("//div[@class='option']"));

        // Iterate through the options while scrolling until the desired one is found
        for (WebElement element : dropDownOptions){
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            String text = element.getText();
            System.out.println("Scrolling to option: "+ text);

            if (selectOption.equals(text)){
                element.click();
                System.out.println("Successfully selected option: "+ text);
                break;
            }
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
