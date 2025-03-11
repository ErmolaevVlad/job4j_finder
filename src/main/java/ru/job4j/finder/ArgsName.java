package ru.job4j.finder;

import java.util.HashMap;
import java.util.Map;
public class ArgsName {
    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '" + key + "' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String str : args) {
            if (!str.startsWith("-")) {
                throw new IllegalArgumentException("Error: This argument '" + str + "' does not start with a '-' character");
            }
            if (!str.contains("=")) {
                throw new IllegalArgumentException("Error: This argument '" + str + "' does not contain an equal sign");
            }
            if (str.split("=", 2)[0].length() < 2) {
                throw new IllegalArgumentException("Error: This argument '" + str + "' does not contain a key");
            }
            if (str.split("=", 2)[1].isEmpty()) {
                throw new IllegalArgumentException("Error: This argument '" + str + "' does not contain a value");
            }
            values.put(str.split("=", 2)[0].split("-")[1], str.split("=", 2)[1]);
        }
    }

    public static ArgsName of(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }
}
