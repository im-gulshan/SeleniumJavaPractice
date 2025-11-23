package MainUtilities.Miscellaneous;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HandleAlertsExample {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");

        // 🔹 1️⃣ Simple Alert
        driver.findElement(By.xpath("//button[text()='Click for JS Alert']")).click();
        Alert simpleAlert = driver.switchTo().alert();
        System.out.println("Simple Alert Text: " + simpleAlert.getText());
        simpleAlert.accept();  // Click OK

        // 🔹 2️⃣ Confirmation Alert
        driver.findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
        Alert confirmAlert = driver.switchTo().alert();
        System.out.println("Confirm Alert Text: " + confirmAlert.getText());
        confirmAlert.dismiss();  // Click Cancel

        // 🔹 3️⃣ Prompt Alert
        driver.findElement(By.xpath("//button[text()='Click for JS Prompt']")).click();
        Alert promptAlert = driver.switchTo().alert();
        System.out.println("Prompt Alert Text: " + promptAlert.getText());
        promptAlert.sendKeys("Selenium Rocks!");
        Thread.sleep(2000);
        promptAlert.accept();  // Click OK

        Thread.sleep(2000);
        driver.quit();
    }
}
