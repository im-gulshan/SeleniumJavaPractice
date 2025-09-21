package MainUtilities.SelfHealingTechnique;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.lang.reflect.Type;

public class SelfHealingLocator {
    private WebDriver driver;
    private static final String HEALED_XPATH_FILE = "src/test/java/MainUtilities/SelfHealingTechnique/healed_locators.json";
    private Map<String, String> healedXpaths;

    public SelfHealingLocator(WebDriver driver){
        this.driver = driver;
        healedXpaths = loadHealedXpaths();
    }

    public WebElement findElement(String originalXpath){
//        String xpathToUse = healedXpaths.getOrDefault(originalXpath, originalXpath);
        String xpathToUse = originalXpath;

        try {
            return driver.findElement(By.xpath(xpathToUse));
        } catch (Exception e){
            try {
                System.out.println("Element not found with XPath: " + xpathToUse);
                System.out.println("Attempting to heal XPath using AI...");
                Thread.sleep(2000);

                String html = driver.getPageSource();
                Thread.sleep(2000);


                // Optional: Only print HTML lengtha for debugging, not entire HTML
                System.out.println("Page source length: " + html.length() + " characters");
                System.out.println(html); // Uncomment this line if you need to see full HTML for debugging

                String healedXpath = GitHubAzureAI.getHealedXpath(originalXpath, html);
                Thread.sleep(2000);
                System.out.println("Healed XPath for: " + originalXpath + " -> " + healedXpath);

                WebElement element = driver.findElement(By.xpath(healedXpath));

                // Save the healed XPath for future use
                healedXpaths.put(originalXpath, healedXpath);
                saveHealedXPaths();

                System.out.println("Successfully found element with healed XPath!");
                return element;

            } catch (Exception ex) {
                System.err.println("Failed to heal XPath: " + originalXpath);
                System.err.println("Error details: " + ex.getMessage());

                // FIXED: Proper NoSuchElementException constructor
                throw new NoSuchElementException("Failed to heal XPath: " + originalXpath + ". Root cause: " + ex.getMessage());
            }
        }
    }

    private Map<String, String> loadHealedXpaths(){
        try {
            if (!Files.exists(Paths.get(HEALED_XPATH_FILE))) {
                System.out.println("No existing healed XPaths file found. Starting fresh.");
                return new HashMap<>();
            }

            String json = new String(Files.readAllBytes(Paths.get(HEALED_XPATH_FILE)));

            // FIXED: Type-safe JSON deserialization
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> loaded = new Gson().fromJson(json, type);

            System.out.println("Loaded " + loaded.size() + " healed XPaths from file.");
            return loaded;

        } catch (IOException e) {
            System.err.println("Error loading healed XPaths file: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveHealedXPaths(){
        try (Writer writer = new FileWriter(HEALED_XPATH_FILE)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();

            gson.toJson(healedXpaths, writer);
            System.out.println("Successfully saved " + healedXpaths.size() + " healed XPaths to file.");

        } catch (IOException e) {
            System.err.println("CRITICAL: Could not save healed XPaths to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
