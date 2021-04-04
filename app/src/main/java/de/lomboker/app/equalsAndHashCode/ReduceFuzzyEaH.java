package de.lomboker.app.equalsAndHashCode;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.FuzzyEqualsAndHashCode.reduceFuzzyEqualsAndHashCode;

@CommandLine.Command(name = "equalsAndHashCode", description = "reduce equals and hash code")
public class ReduceFuzzyEaH implements Runnable {

    @CommandLine.Parameters(index = "0")
    File file;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ReduceFuzzyEaH()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String converted = reduceFuzzyEqualsAndHashCode(code);
            Files.writeString(file.toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
