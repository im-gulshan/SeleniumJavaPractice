package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class HeadlessTest {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void printAllProduct() throws Exception{
//        Thread.sleep(2000);
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        login.click();


        List<WebElement> productLists = driver.findElements(By.xpath("//div[@class='inventory_item_name ']"));

        for (WebElement productList : productLists){
            String productName = productList.getText();
            System.out.println(productName);
        }
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

}
