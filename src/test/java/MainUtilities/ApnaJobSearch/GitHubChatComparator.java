package MainUtilities.ApnaJobSearch;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GitHubChatComparator {

    private static final String CONFIG_PATH = System.getProperty("user.dir") + "/src/test/java/MainUtilities/ApnaJobSearch/envConfig.properties";
    private static String API_KEY;

    static {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            props.load(fis);
            API_KEY = props.getProperty("API_KEY");
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new RuntimeException("API_KEY not found in envConfig.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load envConfig.properties", e);
        }
    }

    private static final String ENDPOINT = "https://models.github.ai/inference";
    private static final String MODEL = "openai/gpt-4.1";

    public static int getMatchScore(String resume, String jobDesc) {
        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(API_KEY))
                .endpoint(ENDPOINT)
                .buildClient();

        String prompt = "Compare the following resume and job description. " +
                "Give a score out of 100 based on skill and experience match. " +
                "Only return the number.\n\nResume:\n" + resume + "\n\nJob Description:\n" + jobDesc;

        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage(""),
                new ChatRequestUserMessage(prompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        options.setModel(MODEL);

        ChatCompletions completions = client.complete(options);
        String content = completions.getChoice().getMessage().getContent().trim();

        System.out.println("Model Response:\n" + content);

        try {
            return Integer.parseInt(content.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            System.err.println("Failed to parse score: " + e.getMessage());
            return 0;
        }
    }
}
