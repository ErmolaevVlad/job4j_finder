package ru.job4j.finder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFilesVisitor extends SimpleFileVisitor<Path> {

    private Map<FileProperty, List<String>> mapFiles = new HashMap<>();
    private Predicate<Path> condition;
    private Pattern pattern;

    public SearchFilesVisitor(Predicate<Path> condition) {
        this.condition = condition;
    }

    public SearchFilesVisitor(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attributes) throws IOException {
        if (pattern != null) {
            Matcher matcher = pattern.matcher(file.getFileName().toString());
            if (matcher.find()) {
                mapFiles.put(new FileProperty(attributes.size(), file.getFileName().toString()),
                        new ArrayList<>(List.of(file.toAbsolutePath().toString())));
            }
        } else if (condition != null) {
            if (condition.test(file)) {
                mapFiles.put(new FileProperty(attributes.size(), file.getFileName().toString()),
                        new ArrayList<>(List.of(file.toAbsolutePath().toString())));
            }
        }
        return super.visitFile(file, attributes);
    }

    public Map<FileProperty, List<String>> getMapFiles() {
        return mapFiles;
    }

    public void fileOutput(ArgsName argsName) throws Exception {
        Map<FileProperty, List<String>> mapFiles = this.getMapFiles();
        StringJoiner joiner = new StringJoiner("");
        for (Map.Entry<FileProperty, List<String>> entry : mapFiles.entrySet()) {
            List<String> path = entry.getValue();
            FileProperty key = entry.getKey();
            StringBuilder builder = new StringBuilder();
            builder.append(key.getName()).append(" - ").append(key.getSize()).append(" byte:");
            joiner.add(builder);
            joiner.add(System.lineSeparator());
            joiner.add(path.toString());
            joiner.add(System.lineSeparator());
        }
        try (FileOutputStream output = new FileOutputStream(argsName.get("o"))) {
            output.write(joiner.toString().getBytes());
        }
    }
}