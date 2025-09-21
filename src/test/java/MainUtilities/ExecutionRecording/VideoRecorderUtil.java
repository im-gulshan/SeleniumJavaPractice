package MainUtilities.ExecutionRecording;

// Monte Media imports
import org.monte.media.av.Format;
import org.monte.media.av.FormatKeys;
import org.monte.media.av.FormatKeys.MediaType;
import org.monte.media.av.Registry;
import org.monte.media.av.codec.video.VideoFormatKeys;
import org.monte.media.math.Rational;
import org.monte.media.screenrecorder.ScreenRecorder;

// Java AWT imports
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

// Java IO imports
import java.io.File;
import java.io.IOException;

// Java Text and Date imports
import java.text.SimpleDateFormat;
import java.util.Date;

// Java Logging import
import java.util.logging.Logger;

public class VideoRecorderUtil extends ScreenRecorder {

    private static final Logger logger = Logger.getLogger(VideoRecorderUtil.class.getName());
    private static final ThreadLocal<ScreenRecorder> screenRecorderThreadLocal = new ThreadLocal<>();
    private static final String DEFAULT_RECORDINGS_DIR = "./test-recordings/";
    private static final int DEFAULT_FRAME_RATE = 15;

    private String name;

    // Constructor
    public VideoRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea,
                             Format fileFormat, Format screenFormat,
                             Format mouseFormat, Format audioFormat,
                             File movieFolder, String name) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    // FIXED: Use the parameter directly, no variable shadowing
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            boolean created = movieFolder.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + movieFolder.getAbsolutePath());
            }
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());

        // FIXED: Use the parameter directly, not getFileFormat()
        String extension = Registry.getInstance().getExtension(fileFormat);

        return new File(movieFolder, name + "_" + timestamp + "." + extension);
    }

    // Start recording method with enhanced error handling
    public static boolean startRecord(String methodName) {
        try {
            // Clean up any existing recorder for this thread
            stopRecord();

            // Create directory for recordings
            File recordingsDir = new File(DEFAULT_RECORDINGS_DIR);
            if (!recordingsDir.exists()) {
                boolean created = recordingsDir.mkdirs();
                if (!created) {
                    logger.severe("Failed to create recordings directory: " + DEFAULT_RECORDINGS_DIR);
                    return false;
                }
            }

            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

            // Get graphics configuration
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            // Create screen recorder with optimized settings
            ScreenRecorder recorder = new VideoRecorderUtil(gc, captureSize,
                    new Format(FormatKeys.MediaTypeKey, MediaType.FILE,
                            FormatKeys.MimeTypeKey, FormatKeys.MIME_QUICKTIME),
                    new Format(FormatKeys.MediaTypeKey, MediaType.VIDEO,
                            VideoFormatKeys.EncodingKey, VideoFormatKeys.ENCODING_QUICKTIME_JPEG,
                            VideoFormatKeys.CompressorNameKey, VideoFormatKeys.COMPRESSOR_NAME_QUICKTIME_JPEG,
                            VideoFormatKeys.DepthKey, 24,
                            VideoFormatKeys.FrameRateKey, Rational.valueOf(DEFAULT_FRAME_RATE),
                            VideoFormatKeys.QualityKey, 1.0f,
                            VideoFormatKeys.KeyFrameIntervalKey, DEFAULT_FRAME_RATE * 60),
                    new Format(FormatKeys.MediaTypeKey, MediaType.VIDEO,
                            VideoFormatKeys.EncodingKey, "black",
                            VideoFormatKeys.FrameRateKey, Rational.valueOf(30)),
                    null, recordingsDir, methodName);

            screenRecorderThreadLocal.set(recorder);
            recorder.start();

            logger.info("Recording started successfully for: " + methodName);
            return true;

        } catch (Exception e) {
            logger.severe("Failed to start recording for " + methodName + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Stop recording method with thread safety
    public static boolean stopRecord() {
        try {
            ScreenRecorder recorder = screenRecorderThreadLocal.get();
            if (recorder != null) {
                recorder.stop();
                screenRecorderThreadLocal.remove();
                logger.info("Recording stopped successfully");
                return true;
            }
            return true; // No recorder to stop, consider it successful

        } catch (Exception e) {
            logger.severe("Failed to stop recording: " + e.getMessage());
            e.printStackTrace();
            screenRecorderThreadLocal.remove(); // Clean up even if stop failed
            return false;
        }
    }

    // Get latest recorded file with better error handling
    public static File getLatestRecordingFile() {
        try {
            File folder = new File(DEFAULT_RECORDINGS_DIR);
            if (!folder.exists() || !folder.isDirectory()) {
                logger.warning("Recordings directory does not exist: " + DEFAULT_RECORDINGS_DIR);
                return null;
            }

            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));
            if (files == null || files.length == 0) {
                logger.warning("No recording files found in: " + DEFAULT_RECORDINGS_DIR);
                return null;
            }

            File latestFile = files[0];
            for (File file : files) {
                if (file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                }
            }

            logger.info("Latest recording file: " + latestFile.getName());
            return latestFile;

        } catch (Exception e) {
            logger.severe("Error finding latest recording file: " + e.getMessage());
            return null;
        }
    }

    // Clean up old recordings (keep only last N files)
    public static void cleanupOldRecordings(int maxFiles) {
        try {
            File folder = new File(DEFAULT_RECORDINGS_DIR);
            if (!folder.exists()) return;

            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));
            if (files == null || files.length <= maxFiles) return;

            // Sort by last modified date (oldest first)
            java.util.Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

            // Delete oldest files
            int filesToDelete = files.length - maxFiles;
            for (int i = 0; i < filesToDelete; i++) {
                if (files[i].delete()) {
                    logger.info("Deleted old recording: " + files[i].getName());
                }
            }

        } catch (Exception e) {
            logger.warning("Error cleaning up old recordings: " + e.getMessage());
        }
    }

    // Get recordings directory path
    public static String getRecordingsDirectory() {
        return new File(DEFAULT_RECORDINGS_DIR).getAbsolutePath();
    }
}
