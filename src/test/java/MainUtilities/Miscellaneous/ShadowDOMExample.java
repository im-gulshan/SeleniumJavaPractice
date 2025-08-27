package MainUtilities.Miscellaneous;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class ShadowDOMExample {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @Test(priority = 1)
    public void handleShadowDOM() throws Exception {
        driver.get("https://books-pwakit.appspot.com/");

        WebElement root = driver.findElement(By.xpath("//book-app"));
        SearchContext searchContext = root.getShadowRoot();


        SearchContext s2 = searchContext.findElement(By.cssSelector("app-header[effects='waterfall']"));
        SearchContext s3 = s2.findElement(By.cssSelector("app-toolbar.toolbar-bottom"));
        SearchContext s4 = s3.findElement(By.cssSelector("book-input-decorator"));

        s4.findElement(By.cssSelector("input#input")).sendKeys("Test");

        Thread.sleep(2000);
    }

    @Test(priority = 2)
    public void testShadowDOM() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html");
        WebElement content = driver.findElement(By.id("content"));

        SearchContext shadowRoot = content.getShadowRoot();
        WebElement textElement = shadowRoot.findElement(By.cssSelector("p"));
        Assert.assertEquals(textElement.getText(), "Hello Shadow DOM", "Text does not match!");

        Thread.sleep(4000);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
