package MainUtilities.PageObjectModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "username")
    WebElement username;

    @FindBy(id = "password")
    WebElement pwd;

    @FindBy(xpath = "//button[@class='radius']/i")
    WebElement loginBtn;

    public void enterUserName(String user){
        username.sendKeys(user);
    }

    public void enterPassword(String password){
        pwd.sendKeys(password);
    }

    public void clickLogin(){
        loginBtn.click();
    }
}
