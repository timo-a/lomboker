package de.lomboker.lib;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReduceNoArgsConstructorTest extends FileTest {

    @Test
    public void shouldConvertPublic() throws IOException {
        String fileName = "Fuzzy/Constructor/NoArgs/Public.java";
        String fileNameRef = "Fuzzy/Constructor/NoArgs/Public.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, NoArgsConstructor.reduceNoArgsConstructor(input));
    }

    @Test
    public void shouldConvertPrivate() throws IOException {
        String fileName = "Fuzzy/Constructor/NoArgs/Private.java";
        String fileNameRef = "Fuzzy/Constructor/NoArgs/Private.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, NoArgsConstructor.reduceNoArgsConstructor(input));
    }

    @Test
    public void shouldConvertProtected() throws IOException {
        String fileName = "Fuzzy/Constructor/NoArgs/Protected.java";
        String fileNameRef = "Fuzzy/Constructor/NoArgs/Protected.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, NoArgsConstructor.reduceNoArgsConstructor(input));
    }

    @Test
    public void shouldConvertPackage() throws IOException {
        String fileName = "Fuzzy/Constructor/NoArgs/Package.java";
        String fileNameRef = "Fuzzy/Constructor/NoArgs/Package.reduced.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, NoArgsConstructor.reduceNoArgsConstructor(input));
    }

}
