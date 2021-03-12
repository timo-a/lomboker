package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @Test
    public void test1() throws IOException {
        String fileName = "ClassAInput.java";
        String fileNameRef = "ClassAReference.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, GetterReducer.reduceGetters(input));

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
