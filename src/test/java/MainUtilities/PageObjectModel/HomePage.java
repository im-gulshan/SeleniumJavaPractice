package MainUtilities.PageObjectModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h4[contains(text(), 'Welcome')]")
    WebElement welcomeText;

    public String getWelcomeMsg(){
        return welcomeText.getText();
    }
}
