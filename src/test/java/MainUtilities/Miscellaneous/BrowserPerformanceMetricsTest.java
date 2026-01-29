package MainUtilities.Miscellaneous;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v142.performance.Performance;
import org.openqa.selenium.devtools.v142.performance.model.Metric;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrowserPerformanceMetricsTest {

    private WebDriver driver;
    private DevTools devTools;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Enable Performance tracking
        devTools.send(Performance.enable(Optional.empty()));
    }

    @Test
    public void extractBrowserPerformanceMetrics() {

        long testStartTime = System.currentTimeMillis();
        driver.get("https://www.selenium.dev");
        long testEndTime = System.currentTimeMillis();

        // ================= REAL PAGE LOAD METRICS =================
        JavascriptExecutor js = (JavascriptExecutor) driver;

        @SuppressWarnings("unchecked")
        Map<String, Object> timing =
                (Map<String, Object>) js.executeScript(
                        "return window.performance.timing.toJSON();");

        long navigationStart = ((Number) timing.get("navigationStart")).longValue();
        long domContentLoaded =
                ((Number) timing.get("domContentLoadedEventEnd")).longValue();
        long loadEventEnd =
                ((Number) timing.get("loadEventEnd")).longValue();

        long domLoadTime = domContentLoaded - navigationStart;
        long fullPageLoadTime = loadEventEnd - navigationStart;

        System.out.println("\n===== REAL PAGE LOAD METRICS =====");
        System.out.println("DOM Content Loaded Time (ms): " + domLoadTime);
        System.out.println("Full Page Load Time (ms): " + fullPageLoadTime);
        System.out.println("Selenium Measured Load Time (ms): " +
                (testEndTime - testStartTime));
        System.out.println("=================================\n");

        // ================= CDP PERFORMANCE METRICS =================
        List<Metric> metrics = devTools.send(Performance.getMetrics());

        System.out.println("===== Browser Performance Metrics =====");

        for (Metric metric : metrics) {
            Number value = metric.getValue();

            if (value instanceof Double) {
                System.out.printf("%s : %.2f%n",
                        metric.getName(), value.doubleValue());
            } else {
                System.out.printf("%s : %d%n",
                        metric.getName(), value.longValue());
            }
        }

        System.out.println("======================================");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
