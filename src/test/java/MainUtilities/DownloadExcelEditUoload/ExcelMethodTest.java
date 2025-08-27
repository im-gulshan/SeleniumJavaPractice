package MainUtilities.DownloadExcelEditUoload;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class ExcelMethodTest extends ExcelFileDownloadEditUploadUtility{
    WebDriver driver;

    // Path of the file after editing
    String downloadedFile = "D:\\SeleniumJavaPractice\\DownloadFiles\\sample3.xls";

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testExcelDownloadUpdate() throws Exception{
        // Open the sample XLS download page
        driver.get("https://filesamples.com/formats/xls");

        // Click on the download link for the XLS file
        WebElement xlFileElement = driver.findElement(By.xpath("//div[text()='XLS / 13.00 KB']/following-sibling::a"));
        xlFileElement.click();

        // Define paths for download and destination
        String downloadPath = System.getProperty("user.home") + "\\Downloads";
        String destinationPath = System.getProperty("user.dir") + "\\DownloadFiles";
        String excelFileName = "sample3.xls";

        // Wait until file is downloaded completely
        boolean isDownloaded = waitForDownload(downloadPath, excelFileName, 10);

        if (isDownloaded){
            System.out.println("*****************\nExcel file is 100% downloaded\n*****************");

            // Copy file from Downloads to project folder
            File sourceDir = new File(downloadPath, excelFileName);
            File destinationDir = new File(destinationPath);
            File destFile = new File(destinationDir, excelFileName);
            copyFile(sourceDir, destFile);

            // Update "December" values in the Excel file
            updateXLSFile(downloadedFile);
        }
    }

    @Test(priority = 2)
    public void testUploadXLS(){
        // Open file upload test page
        driver.get("https://the-internet.herokuapp.com/upload");

        // Select the file for upload
        WebElement chooseFile = driver.findElement(By.id("file-upload"));
        chooseFile.sendKeys(downloadedFile);

        // Click upload button
        WebElement upload = driver.findElement(By.id("file-submit"));
        upload.click();

        // Get confirmation message
        WebElement fileUploadMsg = driver.findElement(By.xpath("//div[@class='example']/h3"));
        String fileUploadText = fileUploadMsg.getText();

        System.out.println("Upload File Msg - "+fileUploadText);
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
