package MainUtilities.SelfHealingTechnique;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GitHubAzureAI {

    private static String API_KEY = "Add Your Key";


    // Correct endpoint and model name
    private static final String ENDPOINT = "https://models.inference.ai.azure.com/chat/completions";
    private static final String MODEL = "gpt-4o-mini"; // Fixed: was "gpt-4.1-mini"

    public static String getHealedXpath(String brokenXpath, String htmlContent) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)   // how long to wait to connect
                .writeTimeout(30, TimeUnit.SECONDS)     // how long to send request data
                .readTimeout(60, TimeUnit.SECONDS)      // how long to wait for AI response
                .build();
        MediaType JSON = MediaType.get("application/json");
        ObjectMapper mapper = new ObjectMapper();

        // System & user prompts
        String systemPrompt = "You are an expert Selenium automation engineer specializing in XPath optimization. " +
                "Your task is to analyze broken XPaths and suggest robust alternatives based on HTML content.";

        String userPrompt = "The following XPath is broken and needs to be fixed: " + brokenXpath +
                "\n\nAnalyze the provided HTML content and suggest a corrected XPath that targets the same logical element. " +
                "Prioritize stable attributes like id, name, class, or data attributes over positional selectors. " +
                "Return ONLY the corrected XPath without any explanation, quotes, or additional text." +
                "\n\nHTML Content:\n" + htmlContent;

        // FIXED: Build JSON programmatically to handle HTML content properly
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);

        List<Map<String, String>> messages = new ArrayList<>();

        // System message
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);

        // User message
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        String json = mapper.writeValueAsString(requestBody);

        // Optional: Log request for debugging
        System.out.println("=== REQUEST DEBUG ===");
        System.out.println("Endpoint: " + ENDPOINT);
        System.out.println("Model: " + MODEL);
        System.out.println("Broken XPath: " + brokenXpath);
        System.out.println("Request JSON length: " + json.length() + " characters");
        System.out.println("===================\n");

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            // FIXED: Enhanced error handling with debugging
            System.out.println("=== RESPONSE DEBUG ===");
            System.out.println("HTTP Code: " + response.code());
            System.out.println("Response Body: " + responseBody);
            System.out.println("=====================\n");

            if (!response.isSuccessful()) {
                System.err.println("HTTP Error: " + response.code() + " - " + response.message());
                System.err.println("Response Body: " + responseBody);
                throw new IOException("API request failed with status: " + response.code() + ", body: " + responseBody);
            }

            // FIXED: Safe JSON parsing with comprehensive error handling
            JsonNode root = mapper.readTree(responseBody);

            // Check if response has error field
            if (root.has("error")) {
                JsonNode errorNode = root.get("error");
                System.err.println("API Error: " + errorNode.toString());
                throw new IOException("API returned error: " + errorNode.toString());
            }

            // Check if choices exists and is not null
            if (!root.has("choices") || root.get("choices").isNull() || root.get("choices").isEmpty()) {
                System.err.println("No valid 'choices' field found in response");
                // Print all available fields for debugging
                System.err.print("Available fields: ");
                root.fieldNames().forEachRemaining(field ->
                        System.err.print(field + " "));
                System.err.println();
                throw new IOException("Invalid API response structure - no choices found");
            }

            JsonNode choices = root.get("choices");
            if (choices.size() == 0) {
                System.err.println("Choices array is empty");
                throw new IOException("API response has empty choices array");
            }

            // Check first choice
            JsonNode firstChoice = choices.get(0);
            if (!firstChoice.has("message") || firstChoice.get("message").isNull()) {
                System.err.println("No 'message' field found in first choice");
                System.err.println("First choice content: " + firstChoice.toString());
                throw new IOException("Invalid choice structure - no message found");
            }

            JsonNode message = firstChoice.get("message");
            if (!message.has("content") || message.get("content").isNull()) {
                System.err.println("No 'content' field found in message");
                System.err.println("Message content: " + message.toString());
                throw new IOException("Invalid message structure - no content found");
            }

            String healedXpath = message.get("content").asText().trim();

            System.out.println("Successfully healed XPath: " + healedXpath);
            return healedXpath;

        } catch (Exception e) {
            System.err.println("Exception occurred during XPath healing: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to heal XPath: " + e.getMessage(), e);
        }
    }
}