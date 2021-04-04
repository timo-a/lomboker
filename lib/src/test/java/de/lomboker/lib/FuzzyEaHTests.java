package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuzzyEaHTests extends FileTest {

    @Test
    public void testFuzzyEaH() throws IOException {
        String fileName = "Fuzzy/EqualsAndHashCode/EaH.java";
        String fileNameRef = "Fuzzy/EqualsAndHashCode/EaH.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzyEqualsAndHashCode.reduceFuzzyEqualsAndHashCode(input));
    }

}
