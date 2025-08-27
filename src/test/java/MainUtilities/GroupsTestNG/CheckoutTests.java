package MainUtilities.GroupsTestNG;

import org.testng.annotations.Test;

public class CheckoutTests {

    @Test(groups = {"smoke"})
    public void addItemToCart(){
        System.out.println("[Smoke] Item added to cart");
    }

    @Test(groups = {"sanity"})
    public void verifyDiscountApplied(){
        System.out.println("[Sanity] Discount applied correctly");
    }

    @Test(groups = {"regression"})
    public void completeCheckoutFlow(){
        System.out.println("[Regression] Checkout flow validated end-to-end");
    }
}
