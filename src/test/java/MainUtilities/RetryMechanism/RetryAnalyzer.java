package MainUtilities.RetryMechanism;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

// Implements TestNG retry mechanism for failed test cases
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY = 2; // Maximum number of retry attempts allowed

    @Override
    public boolean retry(ITestResult iTestResult) {
        // Check if retry limit is not exceeded
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println("Retry test " + iTestResult.getName() +
                    " again | Retry count: " + retryCount);
            return true; // Retry the test
        }
        return false; // Stop retrying after max attempts
    }
}