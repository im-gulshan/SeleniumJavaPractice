package MainUtilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DataDrivenTesting { // 15_July
    @Test(dataProvider = "loginDataProvider")
    public void dataDrivenTesting(String loginUser, String loginPwd){
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.id("login-button"));

        username.sendKeys(loginUser);
        password.sendKeys(loginPwd);
        login.click();

        driver.quit();
    }

    @DataProvider
    public String[][] loginDataProvider() throws Exception {
        File src = new File(System.getProperty("user.dir")+"/src/test/java/TestFiles/SauceDemoLogin.xlsx");
        FileInputStream fis = new FileInputStream(src);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows();

        List<List<String>> list = new ArrayList<>();
        for (int i=0; i<rowCount; i++){
            List<String> temp = new ArrayList<>();

            Row row = sheet.getRow(i);
            Cell userCell = row.getCell(0);
            Cell pwdCell = row.getCell(1);

            String user = userCell.toString();
            String pwd = pwdCell.toString();
            temp.add(user);
            temp.add(pwd);

            list.add(temp);
        }
        int credsPair = list.size();
        String[][] credsPairsAns = new String[credsPair][2];

        for (int i=0; i< list.size(); i++){
            List<String> temp = list.get(i);
            String userName = temp.get(0);
            String pwd = temp.get(1);
            
            credsPairsAns[i][0] = userName;
            credsPairsAns[i][1] = pwd;
        }
        return credsPairsAns;
    }
}
