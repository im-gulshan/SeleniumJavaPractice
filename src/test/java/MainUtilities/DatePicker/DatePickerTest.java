package MainUtilities.DatePicker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class DatePickerTest {
    WebDriver driver;
    DatePickerUtils datePickerUtils;

    // Date you want to select
    String day = "22", month = "January", year = "2023";

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://testautomationpractice.blogspot.com/");

        datePickerUtils = new DatePickerUtils(driver);
    }

    @Test
    public void testDatePicker() {
        datePickerUtils.openDatePicker();
        datePickerUtils.navigateToYear(Integer.parseInt(year));
        datePickerUtils.navigateToMonth(DatePickerUtils.MONTH_MAP.get(month));
        datePickerUtils.selectDay(day);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
