package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReduceNoArgsConstructorTest extends FileTest {

    @Test
    public void testFuzzyToString() throws IOException {
        String fileName = "Fuzzy/Constructor/NoArgs.java";
        String fileNameRef = "Fuzzy/Constructor/NoArgs.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, NoArgsConstructor.reduceNoArgsConstructor(input));
    }

}
