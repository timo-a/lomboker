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
    public void testGetter() throws IOException {
        String fileName = "ClassAWithGetterInput.java";
        String fileNameRef = "ClassAWithLombokGetter.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialGetters.reduceGetters(input));

    }

    @Test
    public void testSetter() throws IOException {
        String fileName = "ClassAWithSetterInput.java";
        String fileNameRef = "ClassAWithLombokSetter.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialSetters.reduceSetters(input));

    }

    @Test
    public void shouldRemoveJavadocForGetter() throws IOException {
        String fileName = "ClassWithGetterWithJavadoc.java";
        String fileNameRef = "ClassWithGetterWithJavadoc.reduced.g.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialGetters.reduceGetters(input));
    }

    @Test
    public void shouldRemoveJavadocForSetter() throws IOException {
        String fileName = "ClassWithSetterWithJavadoc.java";
        String fileNameRef = "ClassWithSetterWithJavadoc.reduced.s.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialSetters.reduceSetters(input));
    }

    @Test
    public void shouldRejectGetterWithAnnotation() throws IOException {
        String fileName = "ClassWithGetterWithAnnotation.java";
        String input = readFile(fileName);
        ClassWrapper cw = new ClassWrapper(input);

        assertFalse(TrivialGetters.isTrivialGetter(cw.methods.get(0), cw.fieldNames));
    }

    @Test
    public void shouldRejectSetterWithAnnotation() throws IOException {
        String fileName = "ClassWithSetterWithAnnotation.java";
        String input = readFile(fileName);
        ClassWrapper cw = new ClassWrapper(input);

        assertFalse(TrivialSetters.isTrivialSetter(cw.methods.get(0), cw.fieldNames));

    }

    //@Test functionality not implemented https://stackoverflow.com/questions/66698723/how-to-put-annotations-on-a-new-line-with-javaparser
    // workaround: use command line tools
    public void getterReducerShouldPlaceAnnotationOnNewLine() throws IOException {
        String fileName = "ClassWithGetterWithAnnotationOnField.java";
        String fileNameRef = "ClassWithGetterWithAnnotationOnField.reduced.g.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialGetters.reduceGetters(input));

    }

    //@Test
    public void setterReducerShouldPlaceAnnotationOnNewLine() throws IOException {
        String fileName = "ClassWithSetterWithAnnotationOnField.java";
        String fileNameRef = "ClassWithSetterWithAnnotationOnField.reduced.s.java";
        String input = readFile(fileName);
        String expected = readFile(fileNameRef);

        assertEquals(expected, TrivialSetters.reduceSetters(input));

    }


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
