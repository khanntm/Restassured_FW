package utils;

public enum Environment {
    DEV("src/test/resources/config-dev.csv"),
    UAT("src/test/resources/config-uat.csv");

    private final String configFilePath;

    Environment(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }
}
