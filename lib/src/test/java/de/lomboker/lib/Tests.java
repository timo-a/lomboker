package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Tests {

    @Test
    public void testGetterMarker() throws IOException {
        String fileName = "ClassBWithGetterInput.java";
        String fileNameRef = "ClassBWithGetterComment.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzyGetterMarker.markFuzzyGetters(input));

    }

    @Test
    public void testSetterMarker() throws IOException {
        String fileName = "ClassBWithSetterInput.java";
        String fileNameRef = "ClassBWithSetterComment.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, FuzzySetterMarker.markFuzzySetters(input));

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
