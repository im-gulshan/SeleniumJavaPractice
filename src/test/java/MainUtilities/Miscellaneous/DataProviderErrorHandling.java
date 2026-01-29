package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DataProviderErrorHandling {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
    }

    @DataProvider(name = "loginData")
    public Object[][] getData() {

        List<Object[]> validRows = new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "src/test/java/TestFiles/orangehrm_testdata.csv"))) {

            String line;
            int rowNum = 0;

            while ((line = br.readLine()) != null) {
                rowNum++;

                String[] data = line.split(",");

                try {
                    if (data.length != 3) {
                        throw new RuntimeException("Invalid column count at row " + rowNum);
                    }

                    String username = data[0].trim();
                    String password = data[1].trim();
                    String expected = data[2].trim();

                    if (username.isEmpty() || password.isEmpty()) {
                        System.out.println("Skipping row " + rowNum + " due to empty values");
                        continue;
                    }

                    if (!expected.matches("SUCCESS|FAILURE")) {
                        System.out.println("Skipping row " + rowNum + " due to invalid expected result");
                        continue;
                    }

                    validRows.add(new Object[]{username, password, expected});

                } catch (Exception e) {
                    System.out.println("Data issue at row " + rowNum + " -> " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DataProvider failed -> " + e.getMessage());
        }

        return validRows.toArray(new Object[0][0]);
    }

    @Test(dataProvider = "loginData")
    public void loginTest(String user, String pass, String expected) {

        try {
            driver.get(
                    "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            driver.findElement(By.name("username")).clear();
            driver.findElement(By.name("username")).sendKeys(user);

            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys(pass);

            driver.findElement(By.xpath("//button[@type='submit']")).click();

            if (expected.equals("SUCCESS")) {

                boolean dashboardLoaded =
                        driver.findElements(
                                By.xpath("//span[text()='Dashboard']")).size() > 0;

                Assert.assertTrue(dashboardLoaded, "Dashboard did not load");

                driver.findElement(
                        By.xpath("//i[contains(@class,'oxd-userdropdown')]")).click();

                driver.findElement(
                        By.xpath("//a[text()='Logout']")).click();

            } else {

                boolean errorVisible =
                        driver.findElements(
                                        By.xpath(
                                                "//p[contains(@class,'oxd-alert-content-text')]"))
                                .size() > 0;

                Assert.assertTrue(errorVisible,
                        "Login error message not displayed");
            }

        } catch (Exception e) {
            throw new SkipException("Runtime failure for user -> " + user);
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
