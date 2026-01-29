package MainUtilities.EnvConfigManagement;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties = new Properties();

    public static void load(String env) {

        if (env == null || env.isEmpty()) {
            env = "dev";
        }

        String fileName = "config-" + env + ".properties";

        try {
            InputStream input = ConfigLoader.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            // Fallback if classloader fails
            if (input == null) {
                String projectPath = System.getProperty("user.dir");
                String filePath = projectPath + "/src/test/resources/" + fileName;
                input = new FileInputStream(filePath);
            }

            properties.load(input);
            System.out.println("Loaded config file: " + fileName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file: " + fileName, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
