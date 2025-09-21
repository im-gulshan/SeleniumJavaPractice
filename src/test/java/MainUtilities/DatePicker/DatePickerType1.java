package MainUtilities.DatePicker;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatePickerType1 {
    WebDriver driver;
    JavascriptExecutor js;
    String day = "22", month = "July", year = "2024";

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://testautomationpractice.blogspot.com/");

        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testDatePicker() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement datePicker = driver.findElement(By.xpath("//input[@id='datepicker']"));
        js.executeScript("arguments[0].scrollIntoView();", datePicker);
        datePicker.click();

        WebElement yearElement = driver.findElement(By.xpath("//span[contains(@class, 'year')]"));
        WebElement monthElement = driver.findElement(By.xpath("//span[contains(@class, 'month')]"));

        String yearText = yearElement.getText();
        String monthText = monthElement.getText();


        WebElement prevElement = driver.findElement(By.xpath("//a[@title = 'Prev']"));
        WebElement nextElement = driver.findElement(By.xpath("//a[@title = 'Next']"));

        int expectedYearInt = Integer.parseInt(year); // 2019 set this
        int actualYearInt = Integer.parseInt(yearText); //2025 Ui

        while (!(month.equals(monthText) && actualYearInt == expectedYearInt)){
            //actual 2024, exp = 2024 --> month = Dec


            if (actualYearInt < expectedYearInt){
                nextElement.click();

                yearElement = driver.findElement(By.xpath("//span[contains(@class, 'year')]"));
                monthElement = driver.findElement(By.xpath("//span[contains(@class, 'month')]"));

                yearText = yearElement.getText();
                monthText = monthElement.getText();

                actualYearInt = Integer.parseInt(yearText); //2025 Ui


            } else if (actualYearInt > expectedYearInt){
                prevElement.click();

                yearElement = driver.findElement(By.xpath("//span[contains(@class, 'year')]"));
                monthElement = driver.findElement(By.xpath("//span[contains(@class, 'month')]"));

                yearText = yearElement.getText();
                monthText = monthElement.getText();

                actualYearInt = Integer.parseInt(yearText); //2025 Ui
            }
            prevElement = driver.findElement(By.xpath("//a[@title = 'Prev']"));
            nextElement = driver.findElement(By.xpath("//a[@title = 'Next']"));
            nextElement = driver.findElement(By.xpath("//a[@title = 'Next']"));
            monthElement = driver.findElement(By.xpath("//span[contains(@class, 'month')]"));
        }

        List<WebElement> daysElement = driver.findElements(By.xpath("//td[contains(@class, ' ')]/a"));

        for (WebElement element : daysElement){
            String dayText = element.getText();
            if (day.equals(dayText)){
                element.click();
                break;
            }
        }

        Thread.sleep(5000);
    }

    public int returnMonthNumber(String month){
        Map<String, Integer> map = new HashMap<>();
        map.put("January", 1);
        map.put("February", 2);
        map.put("March", 3);
        map.put("April", 4);
        map.put("May", 5);
        map.put("June", 6);
        map.put("July", 7);
        map.put("August", 8);
        map.put("September", 9);
        map.put("October", 10);
        map.put("November", 11);
        map.put("December", 12);

        return map.get(month);
    }



    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
