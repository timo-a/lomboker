package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FuzzyTests {

    @Test
    public void testGetterMarker() throws IOException {
        String fileName = "Fuzzy/Getter.java";
        String fileNameRef = "Fuzzy/Getter.marked.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzyGetters.markFuzzyGetters(input));

    }

    @Test
    public void testSetterMarker() throws IOException {
        String fileName = "Fuzzy/Setter.java";
        String fileNameRef = "Fuzzy/Setter.marked.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzySetters.markFuzzySetters(input));

    }

    @Test
    public void shouldKeepAnnotations() throws IOException {
        String fileName = "Fuzzy/SetterWithAnnotation.java";
        String fileNameRef = "Fuzzy/SetterWithAnnotation.marked.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzySetters.markFuzzySetters(input));

    }

    @Test
    public void shouldKeepAnnotationsDebug() throws IOException {
        String input = new StringJoiner("\n")
                .add("class A {")
                .add("")
                .add("    private int number;")
                .add("")
                .add("    @SuppressWarnings(\"javadoc\")")
                .add("    @Override")
                .add("    public void setTheNumber(int i) {")
                .add("        number = i;")
                .add("    }")
                .add("")
                .add("}")
                .add("")
                .toString();

        String actual   = FuzzySetters.markFuzzySetters(input);
        System.out.println(actual);

    }

    @Test
    public void shouldKeepJavadoc() throws IOException {
        String fileName = "Fuzzy/SetterWithJavadoc.java";
        String fileNameRef = "Fuzzy/SetterWithJavadoc.marked.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        String actual = FuzzySetters.markFuzzySetters(input);

        assertEquals(expected, actual);

    }

    @Test
    public void shouldSurviveEmptyJavadoc() throws IOException {
        String fileName = "Fuzzy/SetterWithEmptyJavadoc.java";
        String fileNameRef = "Fuzzy/SetterWithEmptyJavadoc.marked.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzySetters.markFuzzySetters(input));

    }

    private String readFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("file not found! " + fileName);

        File f = new File(resource.getFile());

        return Files.readString(f.toPath());
    }

}
