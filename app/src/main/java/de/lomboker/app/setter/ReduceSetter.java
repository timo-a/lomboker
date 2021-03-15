/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.app.setter;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.TrivialSetters.reduceSetters;

@Command(name = "setter", description = "reduce trivial setters")
public class ReduceSetter implements Runnable {

    @Parameters(index = "0")
    File file;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ReduceSetter()).execute(args);
        System.exit(exitCode);
	}

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String converted = reduceSetters(code);
            Files.writeString(file.toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
