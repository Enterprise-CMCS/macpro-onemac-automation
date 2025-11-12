package gov.cms.onemac.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config.properties", e);
            }
        }
        return properties;
    }

    public static String get(String key) {
        return getProperties().getProperty(key);
    }

    public static String getUsername(String userType) {
        return get(userType + ".username");
    }

    public static String getPassword(String userType) {
        return get(userType + ".password");
    }

    public static String getSEAUsername(String username) {
        return get(username);
    }

    public static String getSEAPassword(String password) {
        return get(password);
    }

}
