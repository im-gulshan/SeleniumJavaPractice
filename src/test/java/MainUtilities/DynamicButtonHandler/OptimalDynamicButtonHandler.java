package MainUtilities.DynamicButtonHandler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OptimalDynamicButtonHandler {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final By CONTAINER_LOCATOR = By.cssSelector("div.widget.HTML[id='HTML5']");

    public OptimalDynamicButtonHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    private WebElement getButton() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(CONTAINER_LOCATOR));
        return container.findElement(By.tagName("button"));
    }

    public String getCurrentState() {
        return getButton().getText().trim();
    }

    public void toggleButton() {
        WebElement button = getButton();
        String beforeState = button.getText();

        button.click();

        // Wait for state change confirmation
        wait.until(driver -> !getButton().getText().equals(beforeState));
        System.out.println("Button toggled from " + beforeState + " to " + getCurrentState());
    }

    public void setButtonState(String targetState) {
        String currentState = getCurrentState();

        if (targetState.equalsIgnoreCase(currentState)) {
            System.out.println("Button already in " + targetState + " state");
            return;
        }

        // Click to change to target state
        toggleButton();

        // Verify we reached the target state
        String finalState = getCurrentState();
        if (!targetState.equalsIgnoreCase(finalState)) {
            throw new RuntimeException("Failed to set button to " + targetState +
                    ". Current state: " + finalState);
        }
    }

    public void clickStart() {
        if ("START".equals(getCurrentState())) {
            toggleButton(); // START -> STOP
        } else {
            // Button is STOP, need to click twice: STOP->START->STOP
            toggleButton(); // STOP -> START
            toggleButton(); // START -> STOP
        }
    }

    public void clickStop() {
        if ("STOP".equals(getCurrentState())) {
            toggleButton(); // STOP -> START
        } else {
            // Button is START, need to click twice: START->STOP->START
            toggleButton(); // START -> STOP
            toggleButton(); // STOP -> START
        }
    }
}
