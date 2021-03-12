package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountTests extends FileTest {

    @Test
    public void testCountTrivialSetters() throws IOException {
        String fileName = "ClassAWithGetterInput.java";
        String code = readFile(fileName);

        int trivialSetters = TrivialSetters.countTrivialSetters(code);
        assertEquals(0, trivialSetters);
    }

}
