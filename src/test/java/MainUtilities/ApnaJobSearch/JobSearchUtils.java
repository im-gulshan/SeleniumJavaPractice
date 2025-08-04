package MainUtilities.ApnaJobSearch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class JobSearchUtils {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    public JobSearchUtils(WebDriver driver){
        this.driver = driver;
    }

    public void waitUntilElementIsVisible(WebElement element){
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitUntilElementIsClickable(WebElement element){
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickElementAtIndex(List<WebElement> elements, int idx){
        waitUntilElementIsVisible(elements.get(0));
        int n = elements.size();
        for (int i=0; i<n; i++){
            if (i==idx) {
                elements.get(i).click();
                break;
            }
        }
    }

    public void clickElementByText (List<WebElement> elements, String str) throws Exception{
        waitUntilElementIsClickable(elements.get(0));
        for (WebElement element : elements) {
            if (element.getText().contains(str)) {
                element.click();
                break;
            }
        }
    }

    public String loadResume() throws IOException {
        return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/java/TestFiles/Resume.txt")));
    }

    public void closeAppDownloadPopUp(){
        WebElement appNudge = driver.findElement(By.xpath("//button[@aria-label='close web to app nudge']"));
        appNudge.click();
    }

    public void searchJob(String jobTitle, String experience, String location) throws Exception {
        WebElement searchJob = driver.findElement(By.xpath("//input[contains(@placeholder, 'Search jobs by')]"));
        searchJob.sendKeys(jobTitle);

        // step 3 : select first option after searching SDET
        List<WebElement> searchResult = driver.findElements(By.xpath("//div[@id='js-navigation-parent']/div"));
        clickElementAtIndex(searchResult, 0);

        // step 4: click on exprience
        WebElement yourExperience = driver.findElement(By.xpath("//div[contains(@data-testid, 'search-experience-input')]"));
        yourExperience.click();

        // Step 4 : select 4 year as exprience
        Thread.sleep(1000);
        List<WebElement> allExpElement = driver.findElements(By.xpath("//div[@class='w-full']/div"));
        clickElementByText(allExpElement, experience);

        // step 5 : sear job for Delhi NCr
        WebElement locationEle = driver.findElement(By.xpath("//input[@placeholder='Search for an area or city']"));
        locationEle.sendKeys(location);


        // step 6 : select delhi NCR
        Thread.sleep(4000);
        List<WebElement> locationSearchResult = driver.findElements(By.xpath("//div[@class='w-full overflow-hidden text-ellipsis whitespace-nowrap text-[#8C8594]']"));
        waitUntilElementIsClickable(locationSearchResult.get(0));
        clickElementByText(locationSearchResult, location);

        //Step 7 : Click on Search Button
        WebElement searchBtn = driver.findElement(By.xpath("//button[contains(@class, 'ApplyButton')]"));
        searchBtn.click();
    }

    public void processJobsAndPrintMatchingLinks(int matchThreshold) throws Exception {
        Thread.sleep(4000);
        List<WebElement> jobs = driver.findElements(By.xpath("//div[contains(@class, 'JobCardList')]//p[@data-testid='job-title']"));
        String parentWindow = driver.getWindowHandle();
        String resumeText = loadResume();

        List<Map<String, String>> matchedJobs = new ArrayList<>();

        for (WebElement job : jobs) {
            String jobTitle = job.getText();
            job.click();

            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!window.equals(parentWindow)) {
                    driver.switchTo().window(window);

                    WebElement showMore = driver.findElement(By.xpath("//button[contains(@class, 'ReadDescriptionButton')]"));
                    waitUntilElementIsClickable(showMore);
                    showMore.click();

                    WebElement jdEle = driver.findElement(By.xpath("//div[contains(@class, 'DescriptionTextFull')]"));
                    waitUntilElementIsClickable(jdEle);
                    String jd = jdEle.getText();

                    int matchScore = GitHubChatComparator.getMatchScore(resumeText, jd);
                    Thread.sleep(1000);
                    if (matchScore >= matchThreshold) {
                        String jobLink = driver.getCurrentUrl();
                        System.out.println("Matched (" + matchScore + "%): " + jobTitle);
                        System.out.println(jobLink);

                        Map<String, String> jobData = new HashMap<>();
                        jobData.put("title", jobTitle);
                        jobData.put("link", jobLink);
                        jobData.put("score", String.valueOf(matchScore));
                        matchedJobs.add(jobData);
                    }

                    driver.close();
                    break;
                }
            }
            driver.switchTo().window(parentWindow);
        }

        if (!matchedJobs.isEmpty()) {
            saveMatchingJobsToExcel(matchedJobs);

            final String CONFIG_PATH = System.getProperty("user.dir") + "/src/test/java/MainUtilities/ApnaJobSearch/envConfig.properties";
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            props.load(fis);
            String botToken = props.getProperty("botToken");
            String chatId = props.getProperty("chatId");

            sendToTelegram(matchedJobs, botToken, chatId);
        } else {
            System.out.println("No matching jobs found.");
        }

    }


    public void saveMatchingJobsToExcel(List<Map<String, String>> matchedJobs) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Matched Jobs");

        // Create Header Row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Job Title");
        header.createCell(1).setCellValue("Job Link");
        header.createCell(2).setCellValue("Match Score");

        // Write data rows
        int rowNum = 1;
        for (Map<String, String> job : matchedJobs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(job.get("title"));
            row.createCell(1).setCellValue(job.get("link"));
            row.createCell(2).setCellValue(job.get("score"));
        }

        // Save to file
        String folderPath = System.getProperty("user.dir") + "/jobs";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = folderPath + "/MatchedJobs_" + timestamp + ".xlsx";
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        }

        workbook.close();
        System.out.println("Excel saved: " + filePath);
    }


    public void sendToTelegram(List<Map<String, String>> matchedJobs, String botToken, String chatId) {
        if (matchedJobs.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("📢 *Matched Job Alerts*\n\n");

        for (Map<String, String> job : matchedJobs) {
            message.append("🧑‍💼 *").append(job.get("title")).append("*\n");
            message.append("🔗 ").append(job.get("link")).append("\n");
            message.append("✅ Score: ").append(job.get("score")).append("%\n\n");
        }

        try {
            String apiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            String payload = "chat_id=" + chatId +
                    "&text=" + URLEncoder.encode(message.toString(), "UTF-8") +
                    "&parse_mode=Markdown";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Telegram message sent successfully!");
            } else {
                System.out.println("❌ Failed to send Telegram message. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("❌ Error sending message to Telegram: " + e.getMessage());
        }
    }



} // JobSearchUtils
