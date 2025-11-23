import com.intuit.karate.Runner;

public class RunnerTest {
    public static void main(String[] args) {
        Runner.path("src/test/java/Karate_Framework/FeatureFiles")
                .parallel(1);
    }
}