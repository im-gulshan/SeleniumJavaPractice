package MainUtilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExtentReportTestng {
    ExtentReports extent;
    WebDriver driver;
    ExtentTest test;

    @BeforeSuite
    public void setUpReport(){
        ExtentSparkReporter reporter = new
                ExtentSparkReporter("htmlReports/execution_report.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("Tester", "Gulshan");
    }

    @BeforeMethod
    public void setUpBrowser(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void openGoogle(){
        test = extent.createTest("Open Google Test");
        driver.get("https://www.google.com/");
        String title = driver.getTitle();
        test.info("Navigated to Google. Title: " + title);

        try {
            Assert.assertNotNull(title);
            Assert.assertTrue(title.contains("Google"), "Title does not contain 'Google'");
            test.pass("Title contains 'Google'");
        } catch (Exception e){
            test.fail("Assertion Failed: " + e.getMessage());
            throw e;  // rethrow to mark test as failed in TestNG
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    @AfterSuite
    public void flushReport(){
        extent.flush();
    }

} // ExtentReport_TestNG
