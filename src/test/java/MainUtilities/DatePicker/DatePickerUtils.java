package MainUtilities.DatePicker;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatePickerUtils {
    private WebDriver driver;
    private JavascriptExecutor js;
    private WebDriverWait wait;

    // Month name to number mapping
    public static final Map<String, Integer> MONTH_MAP = new HashMap<>();
    static {
        MONTH_MAP.put("January", 1);
        MONTH_MAP.put("February", 2);
        MONTH_MAP.put("March", 3);
        MONTH_MAP.put("April", 4);
        MONTH_MAP.put("May", 5);
        MONTH_MAP.put("June", 6);
        MONTH_MAP.put("July", 7);
        MONTH_MAP.put("August", 8);
        MONTH_MAP.put("September", 9);
        MONTH_MAP.put("October", 10);
        MONTH_MAP.put("November", 11);
        MONTH_MAP.put("December", 12);
    }

    // Constructor
    public DatePickerUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Find "Prev" button element
    private WebElement getPrevElement() {
        return driver.findElement(By.xpath("//a[@title = 'Prev']"));
    }

    // Find "Next" button element
    private WebElement getNextElement() {
        return driver.findElement(By.xpath("//a[@title = 'Next']"));
    }

    // Find year display element with wait
    private WebElement getYearElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'year')]")));
    }

    // Find month display element with wait
    private WebElement getMonthElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'month')]")));
    }

    // Scroll to and click the datepicker input to open calendar
    public void openDatePicker() {
        WebElement datePicker = driver.findElement(By.xpath("//input[@id='datepicker']"));
        js.executeScript("arguments[0].scrollIntoView();", datePicker);
        datePicker.click();
    }

    // Navigate to target year
    public void navigateToYear(int expectedYear) {
        int actualYear = Integer.parseInt(getYearElement().getText());

        while (actualYear != expectedYear) {
            if (actualYear < expectedYear) {
                getNextElement().click();
            } else {
                getPrevElement().click();
            }
            actualYear = Integer.parseInt(getYearElement().getText());
        }
    }

    // Navigate to target month
    public void navigateToMonth(int expectedMonth) {
        int actualMonth = MONTH_MAP.get(getMonthElement().getText());

        while (actualMonth != expectedMonth) {
            if (actualMonth < expectedMonth) {
                getNextElement().click();
            } else {
                getPrevElement().click();
            }
            actualMonth = MONTH_MAP.get(getMonthElement().getText());
        }
    }

    // Select specified day from calendar days
    public void selectDay(String expectedDay) {
        List<WebElement> days = driver.findElements(By.xpath("//td[contains(@class, ' ')]/a"));
        for (WebElement dayElement : days) {
            if (dayElement.getText().equals(expectedDay)) {
                dayElement.click();
                break;
            }
        }
    }
}
