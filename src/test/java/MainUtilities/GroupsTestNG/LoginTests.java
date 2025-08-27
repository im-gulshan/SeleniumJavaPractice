package MainUtilities.GroupsTestNG;

import org.testng.annotations.Test;

public class LoginTests {

    @Test(groups = {"smoke"})
    public void verifyLoginPageLoad(){
        System.out.println("[Smoke] Login page loaded successfully");
    }


    @Test(groups = {"sanity"})
    public void loginWithValidUser(){
        System.out.println("[Sanity] Login with valid user");
    }

    @Test(groups = {"regression"})
    public void loginWithInvalidUser(){
        System.out.println("[Regression] Login with invalid user");
    }
}
