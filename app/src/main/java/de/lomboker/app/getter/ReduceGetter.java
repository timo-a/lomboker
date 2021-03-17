/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.app.getter;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.TrivialGetters.reduceGetters;

@Command(name = "getter", description = "reduce trivial getters")
public class ReduceGetter implements Runnable {

    @Parameters(index = "0")
    File file;

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String converted = reduceGetters(code);
            Files.writeString(file.toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}