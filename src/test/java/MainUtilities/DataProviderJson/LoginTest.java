package MainUtilities.DataProviderJson;

import com.google.gson.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.time.Duration;

public class LoginTest {
    WebDriver driver;

    // ✅ DataProvider to read JSON file
    @DataProvider(name = "loginData")
    public Object[][] getDataFromJson() throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader("src/test/java/MainUtilities/DataProviderJson/testData.json"));
//        src/test/java/MainUtilities/DataProviderJson/testData.json
//        src/test/MainUtilities/DataProviderJson/tesDdata.json
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        Object[][] data = new Object[jsonArray.size()][2];

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            data[i][0] = obj.get("username").getAsString();
            data[i][1] = obj.get("password").getAsString();
        }
        return data;
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/"); // Example site
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        // Enter username
        driver.findElement(By.name("username")).sendKeys(username);
        // Enter password
        driver.findElement(By.name("password")).sendKeys(password);
        // Click login button
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Just a sample validation
        boolean isLoginSuccessful = driver.findElements(By.xpath("//p[contains(@class, 'alert')]")).isEmpty();
        Assert.assertTrue(isLoginSuccessful, "Login validation failed for: " + username);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
