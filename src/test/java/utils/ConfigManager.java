package utils;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_FILE = "src/test/resources/config-dev.csv";
    private static Map<String, String> configMap = new HashMap<>();

    static {

        String envName = System.getProperty("env", "dev").toUpperCase();
        Environment environment = Environment.valueOf(envName);
        String configFilePath = environment.getConfigFilePath();

        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCityList(String filePath) {
        List<String> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                cities.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static String get(String key) {
        return configMap.get(key);
    }
}
