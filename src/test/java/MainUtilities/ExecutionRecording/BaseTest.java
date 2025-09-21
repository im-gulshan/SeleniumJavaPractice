package MainUtilities.ExecutionRecording;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class BaseTest implements ITestListener {

    private static final Logger logger = Logger.getLogger(BaseTest.class.getName());
    private static final int MAX_RECORDINGS_TO_KEEP = 10;

    private boolean recordingStarted = false;

    @BeforeSuite
    public void setupRecording() {
        logger.info("=== Recording Strategy: Record All Tests, Keep Only Failed ===");
        logger.info("Recordings directory: " + VideoRecorderUtil.getRecordingsDirectory());

        // Clean up old recordings
        VideoRecorderUtil.cleanupOldRecordings(MAX_RECORDINGS_TO_KEEP);
    }

    // STEP 1: Start recording for EVERY test
    @BeforeMethod
    public void startRecording(Method method) {
        recordingStarted = false;

        try {
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName();
            String testName = className + "_" + methodName;

            boolean success = VideoRecorderUtil.startRecord(testName);
            if (success) {
                recordingStarted = true;
                logger.info("Recording STARTED for: " + testName);
            } else {
                logger.warning("Failed to start recording for: " + testName);
            }

        } catch (Exception e) {
            logger.severe("Exception starting recording: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // STEP 2: Stop recording and decide what to keep
    @AfterMethod
    public void stopRecording(ITestResult result) {
        if (!recordingStarted) {
            logger.info("No recording to process");
            return;
        }

        String testName = result.getMethod().getMethodName();

        try {
            // Always stop recording first
            VideoRecorderUtil.stopRecord();
            logger.info("Recording STOPPED for: " + testName);

            // Decision based on test result
            if (result.getStatus() == ITestResult.FAILURE) {
                // FAILED TEST: Keep the recording
                handleFailedTest(testName, result);
            } else {
                // PASSED/SKIPPED TEST: Delete the recording
                handleNonFailedTest(testName, result.getStatus());
            }

        } catch (Exception e) {
            logger.severe("Exception stopping recording: " + e.getMessage());
            e.printStackTrace();
        } finally {
            recordingStarted = false;
        }
    }

    // Keep recording for failed tests
    private void handleFailedTest(String testName, ITestResult result) {
        logger.severe("TEST FAILED - KEEPING RECORDING: " + testName);

        File recording = VideoRecorderUtil.getLatestRecordingFile();
        if (recording != null) {
            logger.info("RECORDING SAVED: " + recording.getName());

            // Add to system properties for reporting
            System.setProperty("recording." + testName, recording.getAbsolutePath());
            result.setAttribute("recording.path", recording.getAbsolutePath());

            // Log failure reason
            if (result.getThrowable() != null) {
                logger.severe("Failure reason: " + result.getThrowable().getMessage());
            }
        } else {
            logger.warning("No recording file found for failed test!");
        }
    }

    // Delete recording for non-failed tests
    private void handleNonFailedTest(String testName, int status) {
        String statusName = getStatusName(status);
        logger.info("TEST " + statusName + " - DELETING RECORDING: " + testName);

        try {
            File recording = VideoRecorderUtil.getLatestRecordingFile();
            if (recording != null && recording.exists()) {
                boolean deleted = recording.delete();
                if (deleted) {
                    logger.info("Recording DELETED successfully");
                } else {
                    logger.warning("Failed to delete recording: " + recording.getName());
                }
            }
        } catch (Exception e) {
            logger.warning("Error deleting recording: " + e.getMessage());
        }
    }

    private String getStatusName(int status) {
        switch (status) {
            case ITestResult.SUCCESS: return "PASSED";
            case ITestResult.SKIP: return "SKIPPED";
            default: return "UNKNOWN";
        }
    }

    // TestNG Listeners for additional logging
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test STARTED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.severe("Test FAILED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warning("Test SKIPPED: " + result.getMethod().getMethodName());
    }
}
