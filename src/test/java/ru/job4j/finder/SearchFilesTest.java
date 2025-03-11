package ru.job4j.finder;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static ru.job4j.finder.SearchFiles.checkArgument;
import static ru.job4j.finder.SearchFiles.createMask;

class SearchFilesTest {

    @Test
    void whenNameUnknownCreateMask() {
        String mask = "*.txt";
        assertThat(createMask(mask)).isEqualTo("^\\S{1,}.txt");
    }

    @Test
    void whenNameUnknownAndExtensionPartCreateMask() {
        String mask = "*.?xt";
        assertThat(createMask(mask)).isEqualTo("^\\S{1,}.\\Sxt");
    }

    @Test
    void whenMaskIsFullyCreateMask() {
        String mask = "name.class";
        assertThat(createMask(mask)).isEqualTo("^name.class");
    }

    @Test
    void whenMaskTrueCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=*.?xt", "-t=mask", "-o=log.txt"});
        assertThat(checkArgument(jvm)).isTrue();
    }

    @Test
    void whenRegexTrueCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=\\S{1,}\\.?xt", "-t=regex", "-o=log.txt"});
        assertThat(checkArgument(jvm)).isTrue();
    }

    @Test
    void whenNotDirectoryCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=l.txt", "-n=*.?xt", "-t=mask", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("first parameter is not a directory");
    }

    @Test
    void whenMaskIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=* .?xt", "-t=mask", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("second parameter is incorrect. Please check the mask");
    }

    @Test
    void whenRegexIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=x{5,-3}", "-t=regex", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("second parameter is incorrect. Please check the regex");
    }

    @Test
    void whenWordRegexIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=x{5,-3}", "-t=regx", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("third parameter is incorrect");
    }

    @Test
    void whenWordMaskIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=x{5,-3}", "-t=mske", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("third parameter is incorrect");
    }

    @Test
    void whenWordNameIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=x{5,-3}", "-t=neme", "-o=log.txt"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("third parameter is incorrect");
    }

    @Test
    void whenWordNameIsCorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=123.txt", "-t=name", "-o=log.txt"});
        assertThat(checkArgument(jvm)).isTrue();
    }

    @Test
    void whenWordMaskIsCorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=se*.?xt", "-t=mask", "-o=log.txt"});
        assertThat(checkArgument(jvm)).isTrue();
    }

    @Test
    void whenOutputIsIncorrectCheckArgument() {
        ArgsName jvm = ArgsName.of(new String[] {"-d=.", "-n=se*.?xt", "-t=mask", "-o=log.7zip"});
        assertThatThrownBy(() -> checkArgument(jvm)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining("four parameter is incorrect");
    }
}