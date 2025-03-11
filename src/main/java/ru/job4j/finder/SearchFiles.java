package ru.job4j.finder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchFiles {

    public static void search(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Root folder is null. Usage  ROOT_FOLDER.");
        }
        if (args.length != 4) {
            throw new IllegalArgumentException("Incorrect number of parameters");
        }
        ArgsName argsName = ArgsName.of(args);
        if (checkArgument(argsName)) {
            Predicate<Path> condition = null;
            Pattern pattern = null;
            SearchFilesVisitor visitor;
            if (("mask").equals(argsName.get("t"))) {
                pattern = Pattern.compile(createMask(argsName.get("n")));
            } else if (("name").equals(argsName.get("t"))) {
                condition = path -> path.toFile().getName().startsWith(argsName.get("n"));
            } else {
                pattern = Pattern.compile(argsName.get("n"));
            }
            if (pattern != null) {
                visitor = new SearchFilesVisitor(pattern);
            } else {
                visitor = new SearchFilesVisitor(condition);
            }
            Files.walkFileTree(Path.of(argsName.get("d")), visitor);
            visitor.fileOutput(argsName);
        }
    }

    public static String createMask(String mask) {
        char[] chars = mask.toCharArray();
        StringBuilder stringBuilder = new StringBuilder().append("^");
        for (char i : chars) {
            if ('*' == i) {
                 stringBuilder.append("\\S{1,}");
            } else if ('?' == i) {
                stringBuilder.append("\\S");
            } else {
                stringBuilder.append(i);
            }
        }
        return stringBuilder.toString();
    }

    public static boolean checkArgument(ArgsName argsName)  {
        if (!Files.isDirectory(Path.of(argsName.get("d")))) {
            throw new IllegalArgumentException("first parameter is not a directory");
        }
        if (!"mask".equals(argsName.get("t")) & !"regex".equals(argsName.get("t")) & !"name".equals(argsName.get("t"))) {
            throw new IllegalArgumentException("third parameter is incorrect");
        }
        if ("mask".equals(argsName.get("t"))) {
            Pattern pattern = Pattern.compile("\\S{1,}\\.\\S{1,}");
            String text = argsName.get("n");
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()) {
                throw new IllegalArgumentException("second parameter is incorrect. Please check the mask");
            }
        }
        if ("regex".equals(argsName.get("t"))) {
            try {
                Pattern.compile(argsName.get("n"));
            } catch (PatternSyntaxException e) {
                String message = "second parameter is incorrect. Please check the regex";
                throw new IllegalArgumentException(message, e);
            }
        }
        if (!argsName.get("o").endsWith(".txt")) {
            throw new IllegalArgumentException("four parameter is incorrect");
        }
        return true;
    }
}