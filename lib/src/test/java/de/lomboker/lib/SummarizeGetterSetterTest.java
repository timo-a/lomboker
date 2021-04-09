package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummarizeGetterSetterTest extends FileTest {

    @Test
    public void shouldSummarizeAllGettersWhenAllFieldsHaveGetters() throws IOException {
        String fileName = "Summarize/gs/AllGetterSomeSetter.java";
        String fileNameRef = "Summarize/gs/AllGetterSomeSetter.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, SummarizeGetterSetter.summarizeGetters(input));
        assertEquals(input, SummarizeGetterSetter.summarizeSetters(input));
    }

    @Test
    public void shouldNotSummarizeAllSettersWhenNotAllFieldsHaveSetters() throws IOException {
        String fileName = "Summarize/gs/AllGetterSomeSetter.java";
        String fileNameRef = "Summarize/gs/AllGetterSomeSetter.reduced.java";
        String input = readFile(fileName);

        assertEquals(input, SummarizeGetterSetter.summarizeSetters(input));
    }

}
