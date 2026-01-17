package poc;

public class TestConfig {
    public static String baseUrl() {
        // 例: ./mvnw -DbaseUrl=https://reqres.in/api test
        return System.getProperty("baseUrl", "https://reqres.in/api");
    }

    public static boolean enableHttpLog() {
        // 例: ./mvnw -DhttpLog=true test
        return Boolean.parseBoolean(System.getProperty("httpLog", "false"));
    }
}
