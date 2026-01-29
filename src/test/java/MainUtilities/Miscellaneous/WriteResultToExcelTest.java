package MainUtilities.Miscellaneous;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WriteResultToExcelTest {
    WebDriver driver;
    WebDriverWait wait;

    String excelPath = System.getProperty("user.dir")
            + "/src/test/java/TestFiles/TestExecutionData.xlsx";

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        // Kill all Chrome password & credential services
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("profile.password_manager_onboarding", false);

        options.setExperimentalOption("prefs", prefs);

        // Hide automation banner
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // General popup killers
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-save-password-bubble");

        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void runTestsFromExcel() throws Exception {

        File file = new File(excelPath);
        Workbook workbook;
        Sheet sheet;

        // Create Excel if it does not exist
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Sheet1");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("TestCase");
            header.createCell(1).setCellValue("Username");
            header.createCell(2).setCellValue("Password");
            header.createCell(3).setCellValue("Expected");
            header.createCell(4).setCellValue("Result");

            // Sample Data
            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("TC01");
            r1.createCell(1).setCellValue("tomsmith");
            r1.createCell(2).setCellValue("SuperSecretPassword!");
            r1.createCell(3).setCellValue("Success");

            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("TC02");
            r2.createCell(1).setCellValue("wronguser");
            r2.createCell(2).setCellValue("wrongpass");
            r2.createCell(3).setCellValue("Failure");

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
        }

        // Read Excel
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet("Sheet1");

        int lastRow = sheet.getLastRowNum();

        for (int i = 1; i <= lastRow; i++) {
            Row row = sheet.getRow(i);

            String username = row.getCell(1).getStringCellValue();
            String password = row.getCell(2).getStringCellValue();
            String expected = row.getCell(3).getStringCellValue();

            driver.get("https://the-internet.herokuapp.com/login");

            driver.findElement(By.id("username")).clear();
            driver.findElement(By.id("username")).sendKeys(username);

            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys(password);

            driver.findElement(By.cssSelector("button[type='submit']")).click();

            String actualMessage = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")))
                    .getText();

            String result;
            if (expected.equalsIgnoreCase("Success") && actualMessage.contains("You logged into a secure area")) {
                result = "PASS";
            } else if (expected.equalsIgnoreCase("Failure") && actualMessage.contains("Your username is invalid")) {
                result = "PASS";
            } else {
                result = "FAIL";
            }

            row.createCell(4).setCellValue(result);
        }

        fis.close();

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
