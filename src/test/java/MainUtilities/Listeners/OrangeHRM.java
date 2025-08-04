package MainUtilities.Listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class OrangeHRM {
    WebDriver driver;
    @BeforeClass
    public void setUp() throws Exception{
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        Thread.sleep(2000);
    }

    @Test(priority = 1)
    public void testLogo(){
        boolean status = driver.findElement(By.xpath("//img[@alt = 'company-branding']")).isDisplayed();
        Assert.assertEquals(status, true);
    }

    @Test(priority = 2)
    public void testAppURL(){
        Assert.assertEquals(driver.getCurrentUrl(), "https://opensource-demo.orangehrmlive.com");
    }

    @Test(priority = 3, dependsOnMethods = {"testAppURL"})
    public void testHomePageTitle(){
        Assert.assertEquals(driver.getTitle(), "OrangeHRM");
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

}
