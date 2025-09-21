package MainUtilities.Miscellaneous;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ActionsClass {
    WebDriver driver;
    Actions actions;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @Test
    public void testActionClass() throws InterruptedException {

        // ---------------- 1. Double Click ----------------
        driver.get("https://demoqa.com/buttons");
        WebElement doubleClickBtn = driver.findElement(By.id("doubleClickBtn"));
        actions.doubleClick(doubleClickBtn).perform();
        System.out.println("Double click performed");

        // ---------------- 2. Right Click ----------------
        WebElement rightClickBtn = driver.findElement(By.id("rightClickBtn"));
        actions.contextClick(rightClickBtn).perform();
        System.out.println("Right click performed");

        // ---------------- 3. Mouse Hover ----------------
        driver.navigate().to("https://demoqa.com/menu");
        WebElement menu = driver.findElement(By.xpath("(//ul[@id='nav']/li)[2]"));
        actions.moveToElement(menu).perform();
        System.out.println("Mouse hover performed");

        // ---------------- 4. Drag and Drop ----------------
        driver.navigate().to("https://demoqa.com/droppable");
        WebElement source = driver.findElement(By.id("draggable"));
        WebElement destination = driver.findElement(By.id("droppable"));

        actions.dragAndDrop(source, destination).perform();
        System.out.println("Drag and drop performed");

        // ---------------- 5. Keyboard Actions ----------------
        driver.navigate().to("https://demoqa.com/text-box");
        WebElement inputBox = driver.findElement(By.id("userName"));
        inputBox.sendKeys("Selenium WebDriver");

        // Select All (Ctrl+A) -> Copy (Ctrl+C) -> Paste (Ctrl+V)
        actions.click(inputBox)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL)
                .sendKeys(Keys.TAB)
                .keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL)
                .perform();

        System.out.println("Keyboard actions (Ctrl+A, Ctrl+C, Ctrl+V) performed");

        Thread.sleep(2000);
        driver.quit();
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
