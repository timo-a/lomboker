package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountTests {

    @Test
    public void testCountTrivialSetters() throws IOException {
        String fileName = "ClassAWithGetterInput.java";
        String code = readFile(fileName);

        int trivialSetters = TrivialSetters.countTrivialSetters(code);
        assertEquals(0, trivialSetters);
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
