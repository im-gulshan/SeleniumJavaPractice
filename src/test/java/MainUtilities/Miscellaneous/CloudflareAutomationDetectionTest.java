    package MainUtilities.Miscellaneous;

    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.testng.annotations.AfterClass;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    public class CloudflareAutomationDetectionTest {
        private WebDriver driver;

        @BeforeClass
        public void setup() {

            ChromeOptions options = new ChromeOptions();
            // Disable Chrome automation infobars
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }

        @Test
        public void openCloudflareProtectedSite() throws InterruptedException {
            driver.get("https://www.cloudflare.com/");

            // Wait to observe verification / blocking screen
            Thread.sleep(2000);

            System.out.println("Page title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());
        }

        @AfterClass
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }
    }
