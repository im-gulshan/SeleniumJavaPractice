package MainUtilities.Miscellaneous;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class PdfDownloadAndReadTest {

    private WebDriver driver;
    private Path downloadDir;

    @BeforeClass
    public void setup() throws Exception {

        // Create temp download directory
        downloadDir = Files.createTempDirectory("pdf-downloads");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir.toString());
        prefs.put("download.prompt_for_download", false);
        prefs.put("plugins.always_open_pdf_externally", true); // IMPORTANT

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void downloadAndReadPdf() throws Exception {

        // New stable PDF URL
        driver.get("https://www.education.gov.in/sites/upload_files/mhrd/files/upload_document/RUSALogoDesignElements.pdf");

        // Wait until PDF is downloaded
        File pdfFile = waitForPdfDownload(15);

        Assert.assertNotNull(pdfFile, "PDF file was not downloaded");

        // Read PDF content using PDFBox
        String pdfText = readPdfContent(pdfFile);

        System.out.println("\n===== PDF CONTENT =====");
        System.out.println(pdfText);
        System.out.println("=======================\n");
    }

    private File waitForPdfDownload(int timeoutSeconds) throws InterruptedException {

        File pdfFile = null;
        int waited = 0;

        while (waited < timeoutSeconds) {
            File[] files = downloadDir.toFile().listFiles(
                    (dir, name) -> name.toLowerCase().endsWith(".pdf")
            );

            if (files != null && files.length > 0) {
                pdfFile = files[0];

                // Ensure file download is complete
                if (pdfFile.length() > 0) {
                    return pdfFile;
                }
            }

            Thread.sleep(1000);
            waited++;
        }
        return null;
    }

    private String readPdfContent(File pdfFile) throws Exception {

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
