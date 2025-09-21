package MainUtilities.TakeScreenshotOnTestFailure;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListeners implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String screenshotPath = ScreenshotUtil.takeScreenshot(BaseTest.driver, testName);
        System.out.println("Screenshot captured for failed test: " + screenshotPath);
    }
}
