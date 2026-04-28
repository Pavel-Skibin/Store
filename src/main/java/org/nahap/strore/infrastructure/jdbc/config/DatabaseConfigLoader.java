package org.nahap.strore.infrastructure.jdbc.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseConfigLoader {
    private static final String ENV_FILE_NAME = ".env";
    private static final String ENV_URL = "STORE_DB_URL";
    private static final String ENV_USER = "STORE_DB_USER";
    private static final String ENV_PASSWORD = "STORE_DB_PASSWORD";

    private DatabaseConfigLoader() {
    }

    public static DatabaseConfig load() {
        Map<String, String> envFileValues = loadEnvFile();
        String url = readRequired(ENV_URL, envFileValues);
        String user = readRequired(ENV_USER, envFileValues);
        String password = readRequired(ENV_PASSWORD, envFileValues);
        return new DatabaseConfig(url, user, password);
    }

    private static String readRequired(String envKey, Map<String, String> envFileValues) {
        String value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }

        String envFileValue = envFileValues.get(envKey);
        if (envFileValue != null && !envFileValue.trim().isEmpty()) {
            return envFileValue;
        }

        throw new IllegalStateException("Environment variable is required: " + envKey
                + " (set process env or add it to .env in project root)");
    }

    private static Map<String, String> loadEnvFile() {
        Path envPath = Paths.get(ENV_FILE_NAME);
        if (!Files.exists(envPath)) {
            return Map.of();
        }

        Map<String, String> values = new HashMap<>();
        try {
            for (String line : Files.readAllLines(envPath, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                int delimiterIndex = trimmed.indexOf('=');
                if (delimiterIndex <= 0) {
                    continue;
                }

                String key = trimmed.substring(0, delimiterIndex).trim();
                String rawValue = trimmed.substring(delimiterIndex + 1).trim();
                values.put(key, stripQuotes(rawValue));
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read .env file", ex);
        }

        return values;
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
