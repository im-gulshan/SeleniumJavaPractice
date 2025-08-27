package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrokenLinkCheck {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.amazon.in/");
    }

    @Test
    public void testBrokenLink() throws IOException {
        int broken =0, notBroken = 0;
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));

        List<String> urlList = new ArrayList<>();
        for(WebElement element : allLinks){
            String url = element.getAttribute("href");
            urlList.add(url);

            if ((url != null)){
                if((url.contains("www"))){
                    System.out.println(url);
                    if (isLinkBroken(url)){
                        broken++;
                    } else notBroken++;
                }
            }

        }
        System.out.println("Broken count : "+ broken+", Not Broken count : "+notBroken);
    }

    public boolean isLinkBroken(String url) throws IOException {
        URL urlClassObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlClassObject.openConnection();

        int responseCode = connection.getResponseCode();
        return responseCode >= 400;
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
