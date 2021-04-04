package de.lomboker.lib;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class FileTest {
    protected String readFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("file not found! " + fileName);

        File f = new File(resource.getFile());

        return Files.readString(f.toPath());
    }

}
