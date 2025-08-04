package MainUtilities.ApnaJobSearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class JobSearchScraper {
    WebDriver driver;
    JobSearchUtils jobSearchUtils;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://apna.co/");
        jobSearchUtils = new JobSearchUtils(driver);
    }

    @Test
    public void searchJob() throws Exception {
        // step 3: close the android app download
        jobSearchUtils.closeAppDownloadPopUp();

       // step 3 : Search Job
        jobSearchUtils.searchJob("SDET", "6", "Delhi NCR");

        // Step 3 : Match the JD with Resume and print the score
        jobSearchUtils.processJobsAndPrintMatchingLinks(40);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
