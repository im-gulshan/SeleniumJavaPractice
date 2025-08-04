package MainUtilities.JMeter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class SetupJMeter { //19_July
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    @BeforeMethod
    public void setDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://jmeter.apache.org/download_jmeter.cgi");
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void downloadJmeterSetupFile() {
        // Click the ZIP download link and get file name
        String fileName = clickOnJMeterZipLink();
        // Default browser download path
        String downloadPath = System.getProperty("user.home") + "\\Downloads";
        // Destination for extracting files
        String destinationDir = System.getProperty("user.dir") + "\\DownloadFiles";
        // Wait for file to be downloaded
        boolean isDownloaded = waitForDownload(downloadPath, fileName, 5);

        if (isDownloaded) {
            System.out.println("*****************\nJMeter Setup file is 100% downloaded\n*****************");
            File file = new File(downloadPath, fileName);
            try {
                // Extract ZIP to target directory
                JMeterUtils.extractZipFile(file.getAbsolutePath(), destinationDir);
                System.out.println("✅ JMeter ZIP file extracted to: " + destinationDir);

                // Delete ZIP file after successful extraction
                if (file.delete()) {
                    System.out.println("ZIP file deleted after extraction.");
                } else {
                    System.out.println("Unable to delete the ZIP file.");
                }

                // Find extracted folder path
                String extractedDir = JMeterUtils.findExtractedJMeterFolder(destinationDir);
                // Path to jmeter.bat file
                String jmeterBatPath = extractedDir + "\\bin\\jmeter.bat";

                // Create desktop shortcut for JMeter
                JMeterUtils.createWindowsShortcut(jmeterBatPath, "JMeter");
                System.out.println("******** Shortcut created for JMeter on Desktop. ********");

            } catch (Exception e) {
                System.err.println("❌ Failed: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private String clickOnJMeterZipLink() {
        By zipLocator = By.xpath("//h2[@id='binaries']/following-sibling::table//tr/td/a[contains(text(), 'zip')]");
        WebElement jmeterFile = wait.until(ExpectedConditions.visibilityOfElementLocated(zipLocator));
        js.executeScript("arguments[0].scrollIntoView(true)", jmeterFile);
        String fileName = jmeterFile.getText();

        jmeterFile.click(); // Trigger download
        return fileName;
    }

    private boolean waitForDownload(String downloadPath, String fileName, int timeoutInMinutes) {
        File file = new File(downloadPath, fileName);

        FluentWait<File> fw = new FluentWait<>(file)
                .withTimeout(Duration.ofMinutes(timeoutInMinutes))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(Exception.class)
                .withMessage("File is not downloaded");

        return fw.until(downloadedFile -> {
            if (downloadedFile.exists() && downloadedFile.canRead()) {
                return true;
            } else {
                System.out.println("JMeter setup file download is still in progress...");
                return false;
            }
        });
    }

} // SetupJMeter
