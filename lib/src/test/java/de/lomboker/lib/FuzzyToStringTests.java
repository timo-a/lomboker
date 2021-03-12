package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuzzyToStringTests extends FileTest {

    @Test
    public void testFuzzyToString() throws IOException {
        String fileName = "Fuzzy/ToString/ToString.java";
        String fileNameRef = "Fuzzy/ToString/ToString.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzyToString.reduceFuzzyToString(input));
    }

}
