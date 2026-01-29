package MainUtilities.Miscellaneous;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v142.network.Network;
import org.openqa.selenium.devtools.v142.network.model.Request;
import org.openqa.selenium.devtools.v142.network.model.Response;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

public class NetworkLogTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Optional: start with a clean profile
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);

        // Get DevTools and create a session
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Enable Network domain
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        // Add listener for network requests
        devTools.addListener(Network.requestWillBeSent(), request -> {
            Request req = request.getRequest();
            System.out.println("➡ REQUEST: " +
                    req.getMethod() + " " + req.getUrl());
        });

        // Add listener for network responses
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            System.out.println("⬅ RESPONSE: " +
                    res.getStatus() + " " + res.getUrl());
        });
    }

    @Test
    public void captureNetworkLogs() {
        // Hit any URL that triggers network calls
        driver.get("https://www.youtube.com/");

        // You can interact more to generate extra requests if needed
        System.out.println("Page title: " + driver.getTitle());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
