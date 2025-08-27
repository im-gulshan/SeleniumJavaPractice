package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class JavaScriptExecutorOrangeHRM {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testJSExecutorOnOrangeHRM() throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 1) Navigate to OrangeHRM Demo
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        // 2) Set username & password via JavaScriptExecutor
        WebElement username = driver.findElement(By.xpath("//input[@name='username']"));
        WebElement password = driver.findElement(By.xpath("//input[@name='password']"));

        String user = "Admin";
        String pwd = "admin123";
        js.executeScript(
                "arguments[0].value='" + user + "';" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                username
        );

        js.executeScript(
                "arguments[0].value='" + pwd + "';" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                password
        );

        // 3) Click using JavaScriptExecutor
        WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        js.executeScript("arguments[0].click();", loginBtn);


        // 4) Wait for dashboard to load (simple JS check)
        String pageTitle = (String) js.executeScript("return document.title;");
        if (pageTitle.toLowerCase().contains("orangehrm")) {
            System.out.println("Dashboard loads successfully");
        }

        // 5) Scroll down the page
        WebElement target = driver.findElement(By.xpath("//p[text()='Quick Launch']"));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", target);

        // 6) Return current URL via JS
        String currentURL = (String) js.executeScript("return document.URL;");
        Assert.assertTrue(currentURL.contains("dashboard"), "Login failed - URL doesn't contain dashboard");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
