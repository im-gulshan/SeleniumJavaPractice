package MainUtilities.Miscellaneous;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v142.heapprofiler.HeapProfiler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeapSnapshotTest {

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
    }

    @Test
    public void takeHeapSnapshot() throws InterruptedException {
        driver.get("https://www.selenium.dev");

        // Enable Heap Profiler
        devTools.send(HeapProfiler.enable());
        System.out.println("✓ Heap Profiler enabled");

        // Force Garbage Collection for clean snapshot
        devTools.send(HeapProfiler.collectGarbage());
        System.out.println("✓ Garbage collection triggered");

        // Wait for stabilization
        Thread.sleep(2000);

        // Setup listener for addHeapSnapshotChunk events
        AtomicBoolean snapshotTaken = new AtomicBoolean(false);

        devTools.addListener(HeapProfiler.addHeapSnapshotChunk(),
                chunk -> {
                    System.out.println("✓ Snapshot chunk received: " + chunk.length() + " bytes");
                    snapshotTaken.set(true);
                });

        // Trigger Heap Snapshot
        devTools.send(HeapProfiler.takeHeapSnapshot(Optional.of(true), Optional.of(true), Optional.of(true), Optional.of(true)));
        System.out.println("✓ Heap snapshot triggered");

        // Wait for snapshot to complete
        Thread.sleep(3000);

        if (snapshotTaken.get()) {
            System.out.println("✓ Heap snapshot captured successfully!");
        } else {
            System.out.println("⚠ Snapshot may still be processing...");
        }

        // Disable profiler
        devTools.send(HeapProfiler.disable());
        System.out.println("✓ Heap Profiler disabled");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}