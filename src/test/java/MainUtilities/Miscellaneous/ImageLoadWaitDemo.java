package MainUtilities.Miscellaneous;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ImageLoadWaitDemo {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-maximized", "--disable-blink-features=AutomationControlled");
        opts.setPageLoadStrategy(PageLoadStrategy.EAGER);
        driver = new ChromeDriver(opts);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void singleImage_shouldLoad() {
        driver.get("https://www.w3schools.com/html/html_images.asp");
        waitPageReady();
        Assert.assertTrue(waitImage(By.cssSelector("img[src*='lynxlogo']"), 20), "Image load failed");
    }

    @Test
    public void allImages_shouldLoad() {
        driver.get("https://www.w3schools.com/html/html_images.asp");
        waitPageReady();
        Assert.assertTrue(waitAllImages(60), "Images load failed");
    }

    private void waitPageReady() {
        try {
            wait.until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")) ||
                    "interactive".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Page ready check failed: " + e.getMessage());
        }
    }

    private boolean waitImage(By locator, int timeoutSec) {
        try {
            WebElement img = wait.until(d -> {
                List<WebElement> elems = d.findElements(locator);
                return (!elems.isEmpty()) ? elems.get(0) : null;
            });
            return img != null ? waitImageLoad(img, timeoutSec) : (printImages() || false);
        } catch (Exception e) {
            System.out.println("Image not found. Available: ");
            printImages();
            return false;
        }
    }

    private boolean waitImageLoad(WebElement img, int timeoutSec) {
        for (int i = 0; i < 3; i++) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", img);
                Thread.sleep(500);
                ((JavascriptExecutor) driver).executeScript(
                        "var ds = arguments[0].getAttribute('data-src') || arguments[0].getAttribute('data-lazy'); " +
                                "if(ds && !arguments[0].src) arguments[0].src = ds;", img);

                WebElement finalImg = img;
                return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec)).until(d ->
                        Boolean.TRUE.equals(((JavascriptExecutor) d).executeScript(
                                "var im=arguments[0]; return im.complete && im.naturalWidth > 0;", finalImg)));
            } catch (StaleElementReferenceException e) {
                if (i == 2) return false;
                try { Thread.sleep(200); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            } catch (TimeoutException | InterruptedException e) {
                if (i == 2) return false;
            }
        }
        return false;
    }

    private boolean waitAllImages(int timeoutSec) {
        try {
            List<WebElement> imgs = driver.findElements(By.tagName("img"));
            List<WebElement> meaningful = imgs.stream().filter(i -> {
                try {
                    String src = i.getAttribute("src");
                    return src != null && !src.trim().isEmpty() && !src.startsWith("data:");
                } catch (Exception e) { return false; }
            }).toList();

            if (meaningful.isEmpty()) return true;

            int perImgTimeout = Math.max(3, timeoutSec / meaningful.size());
            int[] counts = {0, 0};

            for (WebElement img : meaningful) {
                if (waitImageLoad(img, perImgTimeout)) counts[0]++;
                else counts[1]++;
            }

            double rate = (double) counts[0] / meaningful.size();
            System.out.println(String.format("Images: %d | Success: %d | Failed: %d | Rate: %.1f%%",
                    meaningful.size(), counts[0], counts[1], rate * 100));
            return rate >= 0.8;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private boolean printImages() {
        System.out.println("Available images:");
        driver.findElements(By.tagName("img")).forEach(img -> {
            try {
                String src = img.getAttribute("src");
                System.out.println("  • " + (src != null && src.length() > 50 ? src.substring(0, 50) + "..." : src));
            } catch (Exception e) { }
        });
        return false;
    }
}