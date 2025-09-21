package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.function.Function;

public class WebDriverWaitVsFluentWait {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/"); // Demo login page
    }

    @Test
    public void testWithWebDriverWait() {
        // WebDriverWait (explicit wait)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
        loginBtn.click();
        System.out.println("Clicked Login button using WebDriverWait");
    }

    @Test
    public void testWithFluentWait() {
        // FluentWait (explicit wait with polling + exception handling)
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))       // max wait
                .pollingEvery(Duration.ofMillis(500))      // polling interval
                .ignoring(NoSuchElementException.class);   // ignore this exception

        WebElement loginBtn = fluentWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement element = driver.findElement(By.id("login-button"));
                if (element.isEnabled()) {
                    System.out.println("Login button is enabled!");
                    return element;
                }
                return null;
            }
        });

        loginBtn.click();
        System.out.println("Clicked Login button using FluentWait");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
