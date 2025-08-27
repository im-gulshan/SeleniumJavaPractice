package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class WindowAppInteraction {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.foundit.in/upload");

        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void manageFileUploadUsingRobot() throws Exception{
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // File path to upload
        String filePath = "D:\\SeleniumJavaPractice\\src\\test\\java\\TestFiles\\TestFile.txt";

        // Click on the "Upload Resume" button to start upload process
        WebElement uploadResume = driver.findElement(By.xpath("//div[@class='heroSection-buttonContainer_secondaryBtn secondaryBtn']"));
        uploadResume.click();

        // Find and click the file input element to open file dialog
        WebElement chooseFile = driver.findElement(By.xpath("//input[@id='file-upload']"));
        try {
            chooseFile.click();
        } catch (Exception e) {
            // Use JavaScript click if regular click fails
            js.executeScript("arguments[0].click();", chooseFile);
        }

        Thread.sleep(2000);

        // Use Robot class to handle the Windows file dialog
        uploadFileUsingRobot(filePath);
    }

    @Test(priority = 2)
    public void manageFileUploadWithSendKeys() throws Exception{
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // File path to upload
        String filePath = "D:\\SeleniumJavaPractice\\src\\test\\java\\TestFiles\\TestFile.txt";

        // Click on the "Upload Resume" button to start upload process
        WebElement uploadResume = driver.findElement(By.xpath("//div[@class='heroSection-buttonContainer_secondaryBtn secondaryBtn']"));
        uploadResume.click();

        // Find and click the file input element to open file dialog
        WebElement chooseFile = driver.findElement(By.xpath("//input[@id='file-upload']"));
        chooseFile.sendKeys(filePath);

        Thread.sleep(2000);
    }

    public void uploadFileUsingRobot(String filePath) throws Exception {
        // Copy file path to system clipboard
        StringSelection stringSelection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        Thread.sleep(2000);

        Robot robot = new Robot();

        // Paste the file path using Ctrl+V
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        // Press Enter to confirm file selection
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
