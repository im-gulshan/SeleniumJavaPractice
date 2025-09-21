package MainUtilities.DynamicButtonHandler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class testDynamicButton {
    WebDriver driver;


    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }


    @Test
    public void dynamicButtonTest() throws InterruptedException {
        OptimalDynamicButtonHandler buttonHandler = new OptimalDynamicButtonHandler(driver);

        // Example 1: Simple state check of dynamic button
        System.out.println("Current button state: "+ buttonHandler.getCurrentState());

        // Example 2: set dynamic button STOP state
        buttonHandler.setButtonState("STOP");

        // Example 3: Action-based methods
        buttonHandler.clickStop(); // Click on stop state
        Thread.sleep(2000);
        buttonHandler.clickStart(); // Click on start state

        System.out.println("Button Final state: "+ buttonHandler.getCurrentState());
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
