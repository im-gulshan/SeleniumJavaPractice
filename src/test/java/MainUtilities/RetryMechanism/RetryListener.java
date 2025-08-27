package MainUtilities.RetryMechanism;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// TestNG listener to automatically attach retry analyzer to all test methods
public class RetryListener implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        // Automatically attach RetryAnalyzer to all test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}