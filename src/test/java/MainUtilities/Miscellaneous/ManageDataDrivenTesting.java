package MainUtilities.Miscellaneous;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;

public class ManageDataDrivenTesting {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    // Test method for performing login using data from Excel
    @Test(dataProvider = "readXLData")
    public void dataDrivenLogin(String user, String pwd) throws Exception {
        driver.get("https://www.saucedemo.com/");

        // Locate input fields and login button
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        // Enter credentials and click login
        username.sendKeys(user);
        password.sendKeys(pwd);
        login.click();
    }

    @DataProvider
    public String[][] readXLData() throws Exception {

        // Path to Excel file containing test credentials
        String credFilePath = System.getProperty("user.dir") + "/src/test/java/TestFiles/SauceDemoLogin.xlsx";

        File src = new File(credFilePath);
        FileInputStream fis = new FileInputStream(src);

        // Load Excel workbook and select first sheet
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();

        // Create array to store credentials (excluding header row)
        String[][] credentials = new String[rows - 1][2];
        for (int i = 0; i < rows - 1; i++) {
            Row row = sheet.getRow(i + 1);

            // Read username and password cells
            Cell cellUser = row.getCell(0);
            Cell pwdUser = row.getCell(1);

            String user = cellUser.toString();
            String pwd = pwdUser.toString();

            credentials[i][0] = user;
            credentials[i][1] = pwd;
        }

        return credentials;
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
