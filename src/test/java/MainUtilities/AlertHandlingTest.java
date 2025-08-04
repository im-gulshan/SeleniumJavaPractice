package MainUtilities;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AlertHandlingTest {
    WebDriver driver;
    @BeforeMethod
    public void browserSetup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demo.automationtesting.in/Alerts.html");
    }

    // Reusable method to switch to alert
    public Alert getAlert(){
        return driver.switchTo().alert();
    }

    @Test
    public void handleConfirmationAlert(){
        // Navigate to confirmation alert section
        WebElement element1 = driver.findElement(By.xpath("//a[text()='Alert with OK & Cancel ']"));
        element1.click();

        WebElement element2 = driver.findElement(By.xpath("//button[@onclick='confirmbox()']"));
        element2.click();

        // Handle alert
        Alert alert = getAlert();
        System.out.println("Confirmation Alert: " + alert.getText());
        alert.dismiss(); // or alert.accept()
    }

    @Test
    public void handlePromptAlert(){
        // Navigate to prompt alert section
        WebElement element1 = driver.findElement(By.xpath("//a[text()='Alert with Textbox ']"));
        element1.click();

        WebElement element2 = driver.findElement(By.xpath("//button[@onclick='promptbox()']"));
        element2.click();

        // Handle alert
        Alert alert = getAlert();
        System.out.println("Confirmation Alert: " + alert.getText());
        alert.sendKeys("Gulshan");
        alert.accept(); // or alert.accept()

    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
} // AlertHandlingTest
