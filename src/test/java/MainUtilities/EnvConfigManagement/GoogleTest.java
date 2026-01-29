package MainUtilities.EnvConfigManagement;

import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleTest extends BaseTest {

    @Test
    public void verifyTitle() {
        System.out.println("Title = " + driver.getTitle());
        Assert.assertTrue(driver.getTitle().contains("Google"));
    }
}

