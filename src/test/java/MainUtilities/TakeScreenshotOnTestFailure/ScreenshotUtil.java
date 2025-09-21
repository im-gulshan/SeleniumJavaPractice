package MainUtilities.TakeScreenshotOnTestFailure;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static  String takeScreenshot(WebDriver driver, String testName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String screenshotDir = System.getProperty("user.dir") + File.separator + "Screenshot";
        String filePath = screenshotDir + File.separator + testName + "_" + timeStamp + ".png";

        File dest = new File(filePath);

        File src = ts.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(src, dest);
        } catch (IOException e){
            e.printStackTrace();
        }
        return filePath;
    }
}
