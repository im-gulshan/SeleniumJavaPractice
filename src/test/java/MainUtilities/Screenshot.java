package MainUtilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class Screenshot {
    @Test
    public void takeScreenshot() throws IOException { //14_july
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://websitedemos.net/ceramic-products-store-04/?customize=template");


        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        File target = new File("Screenshot//homepage.png");
        FileUtils.copyFile(src, target);
    }
}
