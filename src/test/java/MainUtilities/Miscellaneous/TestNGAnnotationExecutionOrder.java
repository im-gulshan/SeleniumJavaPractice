package MainUtilities.Miscellaneous;

import org.testng.annotations.*;

public class TestNGAnnotationExecutionOrder {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("1 : BeforeSuite - Executes first, before all tests in the suite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("2 : BeforeTest - Executes before any test cases in <test> tag of testng.xml");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("3 : BeforeClass - Executes before the first method in the current class");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("4 : BeforeMethod - Executes before each @Test method");
    }

    @Test
    public void test1() {
        System.out.println("5 : Test1 - Actual test case execution");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("6 : AfterMethod - Executes after each @Test method");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("7 : AfterClass - Executes after all methods in the current class");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("8 : AfterTest - Executes after all test cases in <test> tag of testng.xml");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("10 : AfterSuite - Executes last, after all tests in the suite");
    }

}
